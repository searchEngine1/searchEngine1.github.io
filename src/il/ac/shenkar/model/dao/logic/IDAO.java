package il.ac.shenkar.model.dao.logic;

import java.util.Set;

/**
 * @author webTech
 *
 */
public interface IDAO<T> {

	/**
	 * Returns all unique DTOs.
	 * @return A collection of unique DTOs
	 */
	public Set<T> getAll();
	
	/**
	 * Get a specific DTO
	 * @param id The unique identifier of the DTO (usually the PK)
	 * @return
	 */
	public T getById(long id);
	
	/**
	 * Insert or update the given DTO
	 * @param toSave The DTO to save
	 * @return
	 */
	public T save(T toSave);
	
	/**
	 * Insert or update the given DTOs
	 * @param toSave The DTOs to save
	 * @return
	 */
	public T[] save(T[] toSave);
	
	/**
	 * Delete a DTO.
	 * @param toDelete The DTO to delete
	 * @return <code>true</code> if the operation was successful, <code>false</code> otherwise
	 */
	public boolean delete(T toDelete);
	
}