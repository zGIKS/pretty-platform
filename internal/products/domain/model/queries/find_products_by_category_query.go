package queries

type FindProductsByCategoryQuery struct {
	category string
	limit    *int
	offset   *int
}

func NewFindProductsByCategoryQuery(category string) (FindProductsByCategoryQuery, error) {
	if category == "" {
		return FindProductsByCategoryQuery{}, nil
	}
	return FindProductsByCategoryQuery{category: category}, nil
}

func (q FindProductsByCategoryQuery) WithPagination(limit, offset int) FindProductsByCategoryQuery {
	q.limit = &limit
	q.offset = &offset
	return q
}

func (q FindProductsByCategoryQuery) Category() string { return q.category }
func (q FindProductsByCategoryQuery) Limit() *int      { return q.limit }
func (q FindProductsByCategoryQuery) Offset() *int     { return q.offset }
