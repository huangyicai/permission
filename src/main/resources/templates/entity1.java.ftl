package ${package.Entity};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
#if(${activeRecord})
import java.io.Serializable;
#end
#foreach($pkg in ${table.importPackages})
import ${pkg};
#end
#if(${entityLombokModel})

import com.baomidou.mybatisplus.annotations.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
#end

/**
* <p>
* $!{table.comment}
* </p>
*
* @author ${author}
* @since ${date}
*/
@Builder
@NoArgsConstructor
@AllArgsConstructor
#if(${entityLombokModel})
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
#end
#if(${table.convert})
@TableName("${table.name}")
@ApiModel(value="$!{table.comment}")
#end
#if(${superEntityClass})
public class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {
#elseif(${activeRecord})
public class ${entity} extends Model<${entity}> {
#else
public class ${entity} implements Serializable {
#end

    private static final long serialVersionUID = 1L;

    ## ----------  BEGIN 字段循环遍历  ----------
    #foreach($field in ${table.fields})
    #if(${field.keyFlag})
    #set($keyPropertyName=${field.propertyName})
    #end
    #if("$!field.comment" != "")
    @ApiModelProperty(value="${field.comment}")
    #end
    #if(${field.keyFlag})
    ## 主键
    #if(${field.keyIdentityFlag})
    @TableId(value = "${field.name}", type = IdType.AUTO)

    #elseif(!$null.isNull(${idType}) && "$!idType" != "")
    @TableId(value = "${field.name}", type = IdType.${idType})
    #elseif(${field.convert})
    @TableId("${field.name}")
    #end
    ## 普通字段
    #elseif(${field.fill})
    ## -----   存在字段填充设置   -----
    #if(${field.convert})
    @TableField(value = "${field.name}", fill = FieldFill.${field.fill})
    #else
    @TableField(fill = FieldFill.${field.fill})
    #end
    #elseif(${field.convert})
    @TableField("${field.name}")
    #end
    ## 乐观锁注解
    #if(${versionFieldName}==${field.name})
    @Version
    #end
    ## 逻辑删除注解
    #if(${logicDeleteFieldName}==${field.name})
    @TableLogic
    #end
    private ${field.propertyType} ${field.propertyName};
    #end
    ## ----------  END 字段循环遍历  ----------

    #if(!${entityLombokModel})
    #foreach($field in ${table.fields})
    #if(${field.propertyType.equals("boolean")})
    #set($getprefix="is")
    #else
    #set($getprefix="get")
    #end

    public ${field.propertyType} ${getprefix}${field.capitalName}() {
    return ${field.propertyName};
    }

    #if(${entityBuilderModel})
    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    #else
    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
    #end
    this.${field.propertyName} = ${field.propertyName};
    #if(${entityBuilderModel})
    return this;
    #end
    }
    #end
    #end

    #if(${entityColumnConstant})
    #foreach($field in ${table.fields})
    public static final String ${field.name.toUpperCase()} = "${field.name}";

    #end
    #end
    #if(${activeRecord})
    @Override
    protected Serializable pkVal() {
    #if(${keyPropertyName})
    return this.${keyPropertyName};
    #else
    return this.id;
    #end
    }

    #end
    #if(!${entityLombokModel})
    @Override
    public String toString() {
    return "${entity}{" +
    #foreach($field in ${table.fields})
    #if($!{velocityCount}==1)
    "${field.propertyName}=" + ${field.propertyName} +
    #else
    ", ${field.propertyName}=" + ${field.propertyName} +
    #end
    #end
    "}";
    }
    #end
}
