package org.dashbuilder.dataset.def;

import org.dashbuilder.dataset.ColumnType;
import org.jboss.errai.common.client.api.annotations.Portable;
import javax.validation.constraints.NotNull;

/**
 * <p>This class is used to define the structure and runtime behaviour of a data set definition's column instance.</p>
 */
@Portable
public class DataColumnDef {
    
    @NotNull(message = "{dataSetApi_dataColumnImpl_id_notNull}")
    private String id;
    @NotNull(message = "{dataSetApi_dataColumnImpl_columnType_notNull}")
    private ColumnType columnType;

    public DataColumnDef() {
    }

    public DataColumnDef(String id, ColumnType columnType) {
        this.id = id;
        this.columnType = columnType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ColumnType columnType) {
        this.columnType = columnType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getId() == null) return false;

        try {
            DataColumnDef d = (DataColumnDef) obj;
            return getId().equals(d.getId());
        } catch (ClassCastException e) {
            return false;
        }
    }
    
    public DataColumnDef clone() {
        return  new DataColumnDef(id, columnType);
    }
}
