package cn.geek51.kun.controller;


import cn.geek51.kun.entity.Product;
import cn.geek51.kun.service.ProductService;
import cn.geek51.util.ResponseUtil;
import cn.geek51.util.UuidUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kun
 * @since 2020-07-06
 */
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    //查询
    @GetMapping("/products")
    public Object list(Integer page,Integer limit,String query) throws Exception {

        HashMap queryMap = new HashMap();
        // 进行拼接, 拼接成一个MAP查询
        if (query != null) {
            queryMap = new ObjectMapper().readValue(query, HashMap.class);
        }

        IPage<Product> product = productService.findList(page, limit, queryMap);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("size",product.getTotal());
        return ResponseUtil.general_response(product.getRecords(),map);
    }

    // 新建
    @PostMapping("/products")
    public Object insertProduct(Product product) {

        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_number",product.getProductNumber());
        Product tempProduct = productService.getOne(queryWrapper);
        if (tempProduct != null){
            return ResponseUtil.general_response(405,"产品型号已存在");
        }
        product.setProductUuid(UuidUtil.getUuid());
        product.setCreateAt(LocalDateTime.now());
        boolean save = productService.save(product);
        return ResponseUtil.general_response(save);
    }

    // 更改
    @PutMapping("/products")
    public Object updateProduct(@RequestBody Product product) {
        System.out.println(product);
        boolean b = productService.updateById(product);
        if (b)
            return ResponseUtil.general_response("success update");
        else
            return ResponseUtil.general_response(ResponseUtil.CODE_EXCEPTION,"更新失败");
    }

    // 删除
    @DeleteMapping("/products/{id}")
    public Object deleteProduct(@PathVariable("id") Integer id) {
        productService.removeById(id);
        return ResponseUtil.general_response("success delete department!");
    }

    // 批量删除
    @DeleteMapping("/products")
    public Object deleteProductBatch(@RequestBody JSONObject params) {
        JSONArray ids = params.getJSONArray("ids");
        List<String> idList = ids.toJavaList(String.class);
        boolean b = productService.removeByIds(idList);
        if (b)
            return ResponseUtil.general_response("success delete department!");
        else
            return ResponseUtil.general_response(ResponseUtil.CODE_EXCEPTION,"删除失败");
    }
}

