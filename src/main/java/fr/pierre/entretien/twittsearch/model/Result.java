package fr.pierre.entretien.twittsearch.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@Entity
@Table(name = "RESULT")
@SequenceGenerator(sequenceName = "SEQ_ID_RESULT", name = "SEQ_RESULT_GEN", initialValue = 1, allocationSize = 1)
@XmlType
@XmlAccessorType(FIELD)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Result {

	@Id
	@Column(name = "RESULT_ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_RESULT_GEN")
	@XmlTransient
	private Integer resultId;

	@Column(name = "RESULT_USER")
	@XmlElement(name = "from_user")
	private String user;

	@Column(name = "RESULT_TEXT")
	@XmlElement(name = "text")
	private String text;

	@XmlTransient
	@ManyToOne(cascade={PERSIST, MERGE, REFRESH }, fetch = EAGER)
	@JoinColumn(name = "TWITTSEARCH_ID")
	private Twittsearch twittsearch;

	public Result() {
	}

	public Result(String user, String text) {
		this.user = user;
		this.text = text;
	}

	public Integer getId() {
		return resultId;
	}

	public void setId(Integer resultId) {
		this.resultId = resultId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Twittsearch getTwittsearch() {
		return twittsearch;
	}

	public void setTwittsearch(Twittsearch twittsearch) {
		this.twittsearch = twittsearch;
	}

}
