package bebetter.basejpa.controller;

import bebetter.basejpa.cfg.CfgWebSocket;
import bebetter.basejpa.enums.EnumSysRoleBase;
import bebetter.basejpa.enums.IDictEnum;
import bebetter.basejpa.enums.ISysRole;
import bebetter.basejpa.model.base.BaseUser;
import bebetter.basejpa.util.FileUploadUtils;
import bebetter.basejpa.util.SpringUtils;
import bebetter.statics.util.V;
import cn.hutool.core.util.StrUtil;
import net.oschina.j2cache.CacheChannel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/common")
public class CommonController {
    /*
     * 获取枚举详情列表
     *
     * @param clazz simpleClassNames 多个类名
     * @return 例如 [{code:'c1',name:'名1'},{code:'c2',name:'名2'}]
     */
    /*@GetMapping("/dictionary/{clazz}")
    public List<Map<String, ?>> dictOne(@PathVariable String clazz) {
        return IDictEnum.Constant.DictMapAll.get(clazz);
    }*/

    /**
     * 批量 获取枚举详情列表
     *
     * @param classes simpleClassNames 多个类名
     */
    @RequestMapping("/dicts")
    public Map<String, List<Map<String, ?>>> dictList(@RequestBody List<String> classes) {
        StrUtil.lowerFirst("");
        return classes.stream().collect(Collectors.toMap(c -> c, c -> V.or(IDictEnum.Constant.DictMapAll.get(c), new ArrayList<>(0))));
    }

    /**
     * 文件上传(图片/其他)
     *
     * @param files  files
     * @param module 预先定义的模块,IEnumUploadModule中的name(也是code)
     * @return 文件已保存, 并返回相对路径  (相对工程根目录例如localhost:8080/)
     */
    @PostMapping("/uploadPre")
    public List<String> upload(@RequestParam(value = "file") MultipartFile[] files, @RequestParam(value = "module") String module) {
        return FileUploadUtils.uploadFiles(files, module);
    }

    /**
     * 刷新缓存
     */
    @RequestMapping("/refreshAllCache")
    public String refreshAllCache() {
        CacheChannel channel = SpringUtils.getBean(CacheChannel.class);
        channel.regions().forEach(region -> channel.clear(region.getName()));
//        BaseMain.getSubTypesOf(IManualCacheService.class).forEach(className ->
//                SpringUtils.getBean(className).reload()//加载需要手动加载的缓存
//        );
        return "ok";
    }


    @GetMapping("/loginNum")
    public Integer loginNum() {
        return bebetter.basejpa.cfg.CfgWebSocket.loginMap.keySet().size();
    }

    @GetMapping("/loginUserIds")
    public Set<Long> loginUserIds() {
        return bebetter.basejpa.cfg.CfgWebSocket.loginMap.get(EnumSysRoleBase.用户).keySet();
    }

    @GetMapping("/loginMap")
    public Map<ISysRole, Map<Long, BaseUser>> loginMapMap() {
        return bebetter.basejpa.cfg.CfgWebSocket.loginMap;
    }

    @GetMapping("/connectNum")
    public Integer connectNum() {
        return CfgWebSocket.sessionIdSet.size();
    }
}
