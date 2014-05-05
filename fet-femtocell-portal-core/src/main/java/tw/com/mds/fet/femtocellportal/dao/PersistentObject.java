package tw.com.mds.fet.femtocellportal.dao;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

@MappedSuperclass
public abstract class PersistentObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long oid;

	@Id
	@GeneratedValue
	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	@Override
	public int hashCode() {
		if(this.getOid() == null) {
			return super.hashCode();
		}
		
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getOid() == null) ? 0 : this.getOid().hashCode());
		return result;
	}

	/**
	 * check the equality of two {@link PersistentObject} by poid.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		PersistentObject other;
		try {
			other = (PersistentObject) obj;
		} catch (ClassCastException e) {
			return false;
		}

		// check if both are entity (with oid)
		if (this.getOid() != null && other.getOid() != null) {
			return this.getOid().equals(other.getOid());
		}
		return false;
	}

	/**
	 * show the poid of this {@link PersistentObject}
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("oid", this.getOid())
				.toString();
	}
	
}
