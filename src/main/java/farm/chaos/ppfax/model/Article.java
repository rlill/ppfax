package farm.chaos.ppfax.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import farm.chaos.ppfax.persistance.Datastore;
import farm.chaos.ppfax.utils.CategoryService;

@Entity
public class Article {

	@Id 	private Long id;
	@Index	private Long categoryId;
	@Ignore private Category categoryRef;
	@Ignore	private String publicUri;
			private String title;
			private String headline;
			private String teasertext;
			private Date dateCreated;
	@Index	private Date dateModified;
			private Date datePublished;
			private String keywords;
			private Long authorId;
	@Ignore private PpUser authorRef;
	@Index	private PublicationStatus status;


	public Article() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		publicUri = null;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
		publicUri = null;
		categoryRef = null;
	}

	public Category getCategory() {
		if (categoryRef != null) return categoryRef;
		if (categoryId == null) return null;
		categoryRef = Datastore.getCategory(categoryId);
		return categoryRef;
	}

	public String getPublicUri() {
		if (publicUri != null) return publicUri;
		if (categoryId == null) return null;
		publicUri = CategoryService.getArticlePath(this);
		return publicUri;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		publicUri = null;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
		publicUri = null;
	}

	public String getTeasertext() {
		return teasertext;
	}

	public void setTeasertext(String teasertext) {
		this.teasertext = teasertext;
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

	public Date getDatePublished() {
		return datePublished;
	}

	public void setDatePublished(Date datePublished) {
		this.datePublished = datePublished;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
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

	public PublicationStatus getStatus() {
		return status;
	}

	public void setStatus(PublicationStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Article [id=");
		builder.append(id);
		builder.append(", categoryId=");
		builder.append(categoryId);
		builder.append(", headline=");
		builder.append(headline);
		builder.append(", authorId=");
		builder.append(authorId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
