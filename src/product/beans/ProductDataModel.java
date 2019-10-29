package product.beans;

import java.util.List;
import javax.faces.model.CollectionDataModel;

public class ProductDataModel extends CollectionDataModel {

    private int rowIndex = -1;
    private int allRowsCount;
    private List list;

    public ProductDataModel(){}

    public ProductDataModel(List list, int allRowsCount)
    {
        this.list = list;
        this.allRowsCount = allRowsCount;
    }

    @Override
    public boolean isRowAvailable() {
        if (list == null) {
            return false;
        }

        int c_rowIndex = getRowIndex();
        if (c_rowIndex >= 0 && c_rowIndex < list.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getRowCount() {
        return allRowsCount;
    }

    public void setRowCount(int partialRowsCount) {
        this.allRowsCount = partialRowsCount;
    }

    @Override
    public Object getRowData() {
        if (list == null) {
            return null;
        } else if (!isRowAvailable()) {
            throw new IllegalArgumentException();
        } else {
            int dataIndex = getRowIndex();
            if (dataIndex < getRowCount()) {
                return list.get(rowIndex);
            } else {
                return null;
            }
        }
    }

    @Override
    public int getRowIndex() {
        return rowIndex;
    }

    @Override
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    @Override
    public Object getWrappedData() {
        return list;
    }

    @Override
    public void setWrappedData(Object hashSet) {
        this.list = (List) hashSet;
    }

}
