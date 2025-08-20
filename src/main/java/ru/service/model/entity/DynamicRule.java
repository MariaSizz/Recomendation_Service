package ru.service.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DynamicRule {

    private Integer id;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("product_text")
    private String productText;
    private List<RuleQuery> rule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleQuery> getRule() {
        return rule;
    }

    public void setRule(List<RuleQuery> rule) {
        this.rule = rule;
    }

}
