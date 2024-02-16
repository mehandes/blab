package org.blab.blender.registry.repository;

import java.util.List;
import java.util.Optional;
import org.blab.blender.registry.domain.SchemaRecord;

public interface SchemaRecordRepository {
  /**
   * Persist new record in registry.
   * 
   * @param record - record to persist
   * @return
   *         <p>
   *         {@code true} if the record saved.
   *         </p>
   *         <p>
   *         {@code false} if a record with the same identifier already exists.
   *         </p>
   * @throws NullPointerException if provided record is {@code null}.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  boolean create(SchemaRecord record);

  /**
   * Update record in registry.
   * 
   * @param record - record to update
   * @return
   *         <p>
   *         {@code true} if the record updated.
   *         </p>
   *         <p>
   *         {@code false} if a record with that identifier does not exists.
   *         </p>
   * @throws NullPointerException if provided record is {@code null}.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  boolean update(SchemaRecord record);

  /**
   * Remove record by given id.
   * 
   * @param id - identifier of the record to be removed
   * @return
   *         <p>
   *         {@code true} if the record removed.
   *         </p>
   *         <p>
   *         {@code false} if a record with that identifier does not exists.
   *         </p>
   * @throws NullPointerException if provided id is {@code null}.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  boolean remove(String id);

  /**
   * Remove all records from registry.
   *
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  void clear();

  /**
   * Retrieve record by given id.
   * 
   * @param id - identifier of the record to be retrieved
   * @return Optional with a required record, if one exists.
   * @throws NullPointerException if provided id is {@code null}.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  Optional<SchemaRecord> getById(String id);

  /**
   * Retrieve record by given topic.
   * 
   * @param id - identifier of the record to be retrieved
   * @return A list of records whose pattern metches a given topic.
   * @throws NullPointerException if provided topic is {@code null}.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  List<SchemaRecord> getByTopic(String topic);

  /**
   * Retrieve all records from registry.
   * 
   * @return A list of all records stored in registry.
   * @throws RepositoryException if there are unrecoverable errors occurs.
   */
  List<SchemaRecord> getAll();
}
