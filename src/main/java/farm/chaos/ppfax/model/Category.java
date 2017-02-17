package farm.chaos.ppfax.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import farm.chaos.ppfax.persistance.Datastore;

@Entity
public class Category {

	@Id 	private Long id;
			private String name;
	@Index	private String path;
			private String sidebarcontent;
	@Index	private Long parentId;
			private Date dateCreated;
	@Index	private Date dateModified;
			private Long authorId;
	@Ignore	private PpUser authorRef;
			private String keywords;
	@Index	private PublicationStatus status;

	public Category() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getSidebarcontent() {
		return sidebarcontent;
	}

	public void setSidebarcontent(String sidebarcontent) {
		this.sidebarcontent = sidebarcontent;
	}

	public Long getParentId() {
		return parentId;
	}

	public List<Category> getBreadcrumb() {
		// TODO: memcache
		List<Category> breadcrumb = new ArrayList<>();
		breadcrumb.add(this);
		Long pi = parentId;
		while (pi != null && pi != 0) {
			Category c = Datastore.getCategory(pi);
			breadcrumb.add(0, c);
			pi = c.getParentId();
		}
		return breadcrumb;
	}

	public List<Category> getSubCategories() {
		// TODO: memcache
		return Datastore.getSubCategories(id);
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
		authorRef = null;
	}

	public PpUser getAuthor() {
		if (authorRef != null) return authorRef;
		if (authorId == null) return null;
		authorRef = Datastore.getPpUser(authorId);
		return authorRef;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public PublicationStatus getStatus() {
		return status;
	}

	public void setStatus(PublicationStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Category [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", path=");
		builder.append(path);
		builder.append(", parentId=");
		builder.append(parentId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
