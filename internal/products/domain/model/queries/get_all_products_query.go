package queries

type GetAllProductsQuery struct {
	limit  *int
	offset *int
}

func NewGetAllProductsQuery() GetAllProductsQuery {
	return GetAllProductsQuery{}
}

func (q GetAllProductsQuery) WithPagination(limit, offset int) GetAllProductsQuery {
	q.limit = &limit
	q.offset = &offset
	return q
}

func (q GetAllProductsQuery) Limit() *int  { return q.limit }
func (q GetAllProductsQuery) Offset() *int { return q.offset }
