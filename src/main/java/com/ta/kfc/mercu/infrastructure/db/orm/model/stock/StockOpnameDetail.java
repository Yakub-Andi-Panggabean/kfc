package com.ta.kfc.mercu.infrastructure.db.orm.model.stock;

import com.ta.kfc.mercu.infrastructure.db.orm.model.asset.Asset;
import com.ta.kfc.mercu.infrastructure.db.orm.model.master.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "stock_opname_detail")
public class StockOpnameDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "stock_opname_id")
    private StockOpname stockOpname;
    @OneToOne
    private Product product;
    private Long qty;
    private Long qtyAdjustment;
    @OneToMany(mappedBy = "stockOpnameDetail")
    private List<StockOpnameAsset> assets;
    private boolean isComplete;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public Long getQtyAdjustment() {
        return qtyAdjustment;
    }

    public void setQtyAdjustment(Long qtyAdjustment) {
        this.qtyAdjustment = qtyAdjustment;
    }

    public StockOpname getStockOpname() {
        return stockOpname;
    }

    public void setStockOpname(StockOpname stockOpname) {
        this.stockOpname = stockOpname;
    }

    public List<StockOpnameAsset> getAssets() {
        return assets;
    }

    public void setAssets(List<StockOpnameAsset> assets) {
        this.assets = assets;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
