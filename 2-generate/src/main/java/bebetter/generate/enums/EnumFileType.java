package bebetter.generate.enums;

import bebetter.generate.model.GenerateEntityInfo;
import bebetter.statics.constant.C;
import bebetter.statics.model.KnowException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;

import java.util.HashMap;
import java.util.Map;

/**
 * 生成文件
 */
@AllArgsConstructor
@Getter
public enum EnumFileType {
    /***/
    admin_vue(
            "vueadmin",
            "vm/adminVue.vm"
    ),
    admin_js(
            "vueadmin",
            "vm/adminJs.vm"
    ),
    vue(
            "vue",
            "vm/vue.vm"
    ),
    js(
            "vue",
            "vm/js.vm"
    ),
    admin_controller(
            "admincontroller",
            "vm/adminController.vm"
    ),
    controller(
            "controller",
            "vm/controller.vm"
            // BaseGiCustomer中定制区分，增改查userId限制的controller / 只查询无userId限制的controller     "vm/controller_userId.vm""vm/controller_query.vm"/
    ),
    service(
            "service",
            "vm/service.vm"
    ),
    dao(
            "dao",
            "vm/dao.vm"
    ),
    dto(
            "dto",
            "vm/dto.vm"
    ),
    ;
    String subFolder;
    String templatePath;

    /**
     * 文件名
     */
    public String getFileName(String name, String nameUp) {
        switch (this) {
            case vue:
                return name + ".vue";
            case js:
                return "mixin_" + name + ".js";
            case admin_vue:
                return "admin" + nameUp + ".vue";
            case admin_js:
                return "mixin_" + name + ".js";
            case admin_controller:
                return "Admin" + nameUp + "Controller.java";
            case controller:
                return nameUp + "Controller.java";
            case service:
                return nameUp + "Service.java";
            case dao:
                return nameUp + "Dao.java";
            case dto:
                return nameUp + "Dto.java";
            default:
                throw new KnowException("还未定义的文件类型:" + this);
        }
    }

    public Template getTemplate(GenerateEntityInfo entity) {
        String entityName = entity.getName();
        Template template = tmpMap.get(entityName + this.name());
        if (null == template) {
            //ResourceNotFoundException
            String path = this.templatePath;
            if (null != path) {
                template = Velocity.getTemplate(path, C.UTF_8_Str);
                tmpMap.put(entityName + this.name(), template);
            }
        }
        return template;
    }

    static Map<String, Template> tmpMap = new HashMap<>();

}
