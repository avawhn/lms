package com.lms.web.controller.lms;

import com.lms.common.annotation.Log;
import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.enums.BusinessType;
import com.lms.common.utils.minio.MinioUtils;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.common.core.domain.entity.Material;
import com.lms.web.service.IMaterialService;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 物料管理Controller
 *
 * @author whn
 * @date 2024-02-21
 */
@RestController
@RequestMapping("/lms/material")
public class MaterialController extends BaseController {
    @Autowired
    private IMaterialService materialService;

    @Resource
    private MinioUtils minioUtils;


    @Value("${minio.duration.get}")
    private int getMethodDuration;

    @Value("${minio.duration.put}")
    private int putMethodDuration;


    /**
     * 查询物料管理列表
     */
    @PreAuthorize("@ss.hasPermi('lms:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(Material material) {
        startPage();
        List<Material> list = materialService.selectMaterialList(material);
        list.forEach(m -> {
            String url = minioUtils
                    .generatePresignedObjectUrl(
                            Method.GET,
                            "lms", m.getImageUrl(),
                            putMethodDuration, TimeUnit.SECONDS,
                            null
                    );
            m.setImageUrl(url);
        });
        return getDataTable(list);
    }

    /**
     * 导出物料管理列表
     */
    @PreAuthorize("@ss.hasPermi('lms:material:export')")
    @Log(title = "物料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Material material) {
        List<Material> list = materialService.selectMaterialList(material);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        util.exportExcel(response, list, "物料管理数据");
    }

    /**
     * 获取物料管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('lms:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(materialService.selectMaterialById(id));
    }

    /**
     * 新增物料管理
     */
    @PreAuthorize("@ss.hasPermi('lms:material:add')")
    @Log(title = "物料管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Material material) {
        return toAjax(materialService.insertMaterial(material));
    }

    /**
     * 修改物料管理
     */
    @PreAuthorize("@ss.hasPermi('lms:material:edit')")
    @Log(title = "物料管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Material material) {
        return toAjax(materialService.updateMaterial(material));
    }

    /**
     * 删除物料管理
     */
    @PreAuthorize("@ss.hasPermi('lms:material:remove')")
    @Log(title = "物料管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(materialService.deleteMaterialByIds(ids));
    }
}
