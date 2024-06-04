package com.lms.web.controller.lms;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.Article;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文章Controller
 *
 * @author ruoyi
 * @date 2024-05-19
 */
@RestController
@RequestMapping("/lms/article")
public class ArticleController extends BaseController {
    @Autowired
    private IArticleService articleService;

    /**
     * 查询文章列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Article article) {
        startPage();
        List<Article> list = articleService.selectArticleList(article);
        return getDataTable(list);
    }

    /**
     * 导出文章列表
     */
    @PostMapping("/export")
    public void export(HttpServletResponse response, Article article) {
        List<Article> list = articleService.selectArticleList(article);
        ExcelUtil<Article> util = new ExcelUtil<Article>(Article.class);
        util.exportExcel(response, list, "文章数据");
    }

    /**
     * 获取文章详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(articleService.selectArticleById(id));
    }

    /**
     * 新增文章
     */
    @PostMapping
    public AjaxResult add(@RequestBody Article article) {
        return toAjax(articleService.insertArticle(article));
    }

    /**
     * 修改文章
     */
    @PutMapping
    public AjaxResult edit(@RequestBody Article article) {
        return toAjax(articleService.updateArticle(article));
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(articleService.deleteArticleByIds(ids));
    }
}
