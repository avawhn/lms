package com.lms.web.controller.lms;

import com.lms.common.core.controller.BaseController;
import com.lms.common.core.domain.AjaxResult;
import com.lms.common.core.domain.entity.CompetitionVideo;
import com.lms.common.core.page.TableDataInfo;
import com.lms.common.utils.minio.MinioUtils;
import com.lms.common.utils.poi.ExcelUtil;
import com.lms.web.service.ICompetitionVideoService;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 比赛视频Controller
 *
 * @author ruoyi
 * @date 2024-05-20
 */
@RestController
@RequestMapping("/lms/competition/video")
public class CompetitionVideoController extends BaseController {
    @Autowired
    private ICompetitionVideoService competitionVideoService;

    @Resource
    private MinioUtils minioUtils;


    @Value("${minio.duration.get}")
    private int getMethodDuration;

    @Value("${minio.duration.put}")
    private int putMethodDuration;


    /**
     * 查询比赛视频列表
     */
    // @PreAuthorize("@ss.hasPermi('system:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(CompetitionVideo competitionVideo) {
        startPage();
        List<CompetitionVideo> list = competitionVideoService.selectCompetitionVideoList(competitionVideo);
        return getDataTable(list);
    }


    @GetMapping("/grade/{grade}")
    public AjaxResult getListByGrade(@PathVariable("grade") Integer grade) {
        CompetitionVideo video = new CompetitionVideo();
        video.setGrade(grade);
        List<CompetitionVideo> list = competitionVideoService.selectCompetitionVideoList(video);
        list.forEach(v -> {
            String name = v.getUrl();
            String url = minioUtils.generatePresignedObjectUrl(
                    Method.GET,
                    "lms", name,
                    getMethodDuration, TimeUnit.SECONDS,
                    null
            );
            v.setUrl(url);
        });
        return success(list);
    }

    /**
     * 导出比赛视频列表
     */
    // @PreAuthorize("@ss.hasPermi('system:video:export')")
    // @Log(title = "比赛视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CompetitionVideo competitionVideo) {
        List<CompetitionVideo> list = competitionVideoService.selectCompetitionVideoList(competitionVideo);
        ExcelUtil<CompetitionVideo> util = new ExcelUtil<CompetitionVideo>(CompetitionVideo.class);
        util.exportExcel(response, list, "比赛视频数据");
    }

    /**
     * 获取比赛视频详细信息
     */
    // @PreAuthorize("@ss.hasPermi('system:video:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Integer id) {
        return success(competitionVideoService.selectCompetitionVideoById(id));
    }

    /**
     * 新增比赛视频
     */
    // @PreAuthorize("@ss.hasPermi('system:video:add')")
    // @Log(title = "比赛视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CompetitionVideo competitionVideo) {
        return toAjax(competitionVideoService.insertCompetitionVideo(competitionVideo));
    }

    /**
     * 修改比赛视频
     */
    // @PreAuthorize("@ss.hasPermi('system:video:edit')")
    // @Log(title = "比赛视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CompetitionVideo competitionVideo) {
        return toAjax(competitionVideoService.updateCompetitionVideo(competitionVideo));
    }

    /**
     * 删除比赛视频
     */
    // @PreAuthorize("@ss.hasPermi('system:video:remove')")
    // @Log(title = "比赛视频", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Integer[] ids) {
        return toAjax(competitionVideoService.deleteCompetitionVideoByIds(ids));
    }
}
