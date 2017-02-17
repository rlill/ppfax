package farm.chaos.ppfax.model;

import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Ignore;
import com.googlecode.objectify.annotation.Index;

import farm.chaos.ppfax.persistance.Datastore;

@Entity
public class Image {

	@Id 	private Long id;
			private String title;
	@Index	private String storagePath;
			private Date dateCreated;
	@Index	private Date dateModified;
			private Long authorId;
	@Ignore	private PpUser authorRef;
	@Index	private PublicationStatus status;

	public Image() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStoragePath() {
		return storagePath;
	}

	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}

	public String getImageUrl() {
		return storagePath;
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

	public PublicationStatus getStatus() {
		return status;
	}

	public void setStatus(PublicationStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Image [id=");
		builder.append(id);
		builder.append(", storagePath=");
		builder.append(storagePath);
		builder.append(", authorId=");
		builder.append(authorId);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}

}
