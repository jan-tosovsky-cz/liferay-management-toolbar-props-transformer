package my.project.util.search;

public class OrderByContext {

    private final String orderByCol;
    private final String orderByType;
    private final String sortKey;
    private final Integer sortType;

    public OrderByContext(String orderByCol, String orderByType, String sortKey, Integer sortType) {
        this.orderByCol = orderByCol;
        this.orderByType = orderByType;
        this.sortKey = sortKey;
        this.sortType = sortType;
    }

    public String getOrderByCol() {
        return orderByCol;
    }

    public String getOrderByType() {
        return orderByType;
    }

    public String getSortKey() {
        return sortKey;
    }

    public Integer getSortType() {
        return sortType;
    }

}
