/*
package bebetter.basejpa.model.base;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Id;

@SQLDelete(sql = "UPDATE " + "soft_delete_example" + " SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
@Entity
public class LogicDeleteModelDemo {
    @Id
    Long id;
    String name;
    String name1;
    String name2;
    String name3;
    Boolean deleted;
}
*/
