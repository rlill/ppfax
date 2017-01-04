package farm.chaos.ppfax.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Paragraph {

	@Id 	private Long id;
			private Long articleId;
			private String headline;
			private String bodyText;
	@Index	private Integer sequence;
			private Date dateCreated;
			private Date dateModified;
			private ParagraphStyle style;
			private Long imageId;
			private PublicationStatus status;

	public Paragraph() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getBodyText() {
		return bodyText;
	}

	public void setBodyText(String bodyText) {
		this.bodyText = bodyText;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
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

	public ParagraphStyle getStyle() {
		return style;
	}

	public void setStyle(ParagraphStyle style) {
		this.style = style;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
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
		builder.append("Paragraph [id=");
		builder.append(id);
		builder.append(", articleId=");
		builder.append(articleId);
		builder.append(", sequence=");
		builder.append(sequence);
		builder.append(", style=");
		builder.append(style);
		builder.append(", imageId=");
		builder.append(imageId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
