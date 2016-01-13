package com.zhwyd.server.bean;
// default package
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.gotop.framework.core.domain.StampBaseEntity;
/**
 * ResourceTable entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "resource", uniqueConstraints = {})
@AttributeOverride(name = "id", column = @Column(name = "reso_id"))
public class ResourceTable extends StampBaseEntity {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // Fields
    private ResourceTable     resourceTable;
    private String            resoName;
    private String            resoCode;
    private String            resoUrl;
    private Integer           resoNo;
    private Integer           resoLevel;
    private String            aId;
    private String            resoClass;
    private String			  resoNameVietnam; //越南语
    private String			  resoNameEnglish;//英语
    private String			  resoNameJapanese;//日文
    private String			  resoNameTraditionalChinese;//繁体中文
    // Constructors
    /** default constructor */
    public ResourceTable() {
    }

    // Property accessors
    @Column(name = "reso_name", length = 30)
    public String getResoName() {
        return this.resoName;
    }

    public void setResoName(String resoName) {
        this.resoName = resoName;
    }

    @Column(name = "reso_code", length = 30)
    public String getResoCode() {
        return this.resoCode;
    }

    public void setResoCode(String resoCode) {
        this.resoCode = resoCode;
    }

    @Column(name = "reso_url", length = 512)
    public String getResoUrl() {
        return this.resoUrl;
    }

    public void setResoUrl(String resoUrl) {
        this.resoUrl = resoUrl;
    }

    @Column(name = "reso_no")
    public Integer getResoNo() {
        return this.resoNo;
    }

    public void setResoNo(Integer resoNo) {
        this.resoNo = resoNo;
    }

    @Column(name = "reso_level")
    public Integer getResoLevel() {
        return this.resoLevel;
    }

    public void setResoLevel(Integer resoLevel) {
        this.resoLevel = resoLevel;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "par_id")
    public ResourceTable getResourceTable() {
        return resourceTable;
    }

    public void setResourceTable(ResourceTable resourceTable) {
        this.resourceTable = resourceTable;
    }
    
    @Column(name = "a_id",length = 10)
    public String getAId() {
        return aId;
    }

    public void setAId(String id) {
        aId = id;
    }

    @Column(name = "reso_class",length = 20)
    public String getResoClass() {
        return resoClass;
    }

    public void setResoClass(String resoClass) {
        this.resoClass = resoClass;
    }
    
    @Column(name = "reso_name_vietnam", length = 100)
	public String getResoNameVietnam() {
		return resoNameVietnam;
	}

	public void setResoNameVietnam(String resoNameVietnam) {
		this.resoNameVietnam = resoNameVietnam;
	}
	
	@Column(name = "reso_name_english", length = 100)
	public String getResoNameEnglish() {
		return resoNameEnglish;
	}

	public void setResoNameEnglish(String resoNameEnglish) {
		this.resoNameEnglish = resoNameEnglish;
	}
	
	@Column(name = "reso_name_japanese", length = 100)
	public String getResoNameJapanese() {
		return resoNameJapanese;
	}

	public void setResoNameJapanese(String resoNameJapanese) {
		this.resoNameJapanese = resoNameJapanese;
	}
	
	@Column(name = "reso_name_traditional_chinese", length = 100)
	public String getResoNameTraditionalChinese() {
		return resoNameTraditionalChinese;
	}

	public void setResoNameTraditionalChinese(String resoNameTraditionalChinese) {
		this.resoNameTraditionalChinese = resoNameTraditionalChinese;
	}
    
    
}