package com.hoioy.diamond.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: BootStrapTreeViewDTO
 *
 * @Description: bootstraptree-view实体
 * @Author wanghw
 * @CreatDate 2018年1月24日
 */
@Data
@NoArgsConstructor
public class BootStrapTreeViewDTO {
    //节点ID
    //树的节点Id，区别于数据库中保存的数据Id。若要存储数据库数据的Id，添加新的Id属性；若想为节点设置路径，类中添加Path属性
    private String nodeId;
    private String id;
    //名称
    private String text;
    private BootStrapTreeState state;
    //子节点
    //子节点，可以用递归的方法读取，方法在下一章会总结
    private List<BootStrapTreeViewDTO> nodes = new ArrayList();

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public BootStrapTreeState getState() {
        return state;
    }

    public void setState(BootStrapTreeState state) {
        this.state = state;
    }

    public List<BootStrapTreeViewDTO> getNodes() {
        return nodes;
    }

    public void setNodes(List<BootStrapTreeViewDTO> nodes) {
        this.nodes = nodes;
    }
}
