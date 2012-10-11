package fr.pierre.entretien.twittsearch.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.xml.bind.annotation.XmlAccessType.FIELD;

@Entity
@Table(name = "twittsearch")
@SequenceGenerator(sequenceName = "SEQ_ID_TWITTSEARCH", name = "SEQ_TWITTSEARCH_GEN", initialValue = 1, allocationSize = 1)
@XmlType
@XmlAccessorType(FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Twittsearch {

	@Id
	@Column(name = "TWITTSEARCH_ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_TWITTSEARCH_GEN")
	private Integer id;

	@Column(name = "TWITTSEARCH_QUERY")
	@XmlElement(name = "query")
	private String query;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TWITTSEARCH_DATE_QUERY")
	private Date dateQuery = new Date();

	@OneToMany(mappedBy = "twittsearch", orphanRemoval = true, cascade={PERSIST, MERGE, REFRESH }, fetch = EAGER)
	@XmlElement(name = "results")
	private Set<Result> results ;

	public Twittsearch() {
	}

	public Twittsearch(String query) {
		this.query = query;
		this.dateQuery = new Date();
		results = new HashSet();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuery() {
		return query;
	}

	public Date getDateQuery() {
		return dateQuery;
	}

	public Set<Result> getResults() {
		return results;
	}
}
