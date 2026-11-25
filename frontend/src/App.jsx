
import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [search, setSearch] = useState("");
  const [category, setCategory] = useState("");
  const [sort, setSort] = useState("id");
  const [page, setPage] = useState(1);
  const [size, setSize] = useState(10);

  const [totalPages, setTotalPages] = useState(1);
const [total, setTotal] = useState(0);

  useEffect(() => {
    const fetchProducts = async () => {
      setLoading(true);

      try {
        const params = new URLSearchParams({
  page,
  size,
  q: search,
  category,
  sort,
});

        const response = await fetch(
  `https://paginated-product-catalog-api.onrender.com/api/products?${params}`
);

        if (!response.ok) {
          throw new Error("Failed to fetch products");
        }

        const data = await response.json();
        setProducts(data.items);
setTotalPages(data.totalPages);
setTotal(data.total);
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
    }, [page, search, category, sort, size]);

  return (
    <div className="app">
      <h1>Product Catalog</h1>
      <p>Explore our products</p>

      <div className="controls">
        <input
          type="text"
          placeholder="Search products..."
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(1);
          }}
        />
        <select
  value={category}
  onChange={(e) => {
    setCategory(e.target.value);
    setPage(1);
  }}
>
  <option value="">All Categories</option>
  <option value="Electronics">Electronics</option>
  <option value="Accessories">Accessories</option>
  <option value="Fashion">Fashion</option>
  <option value="Home">Home</option>
</select>

        <select
          value={sort}
          onChange={(e) => {
            setSort(e.target.value);
            setPage(1);
          }}
        >
          <option value="id">Default</option>
          <option value="price_asc">Price: Low to High</option>
          <option value="price_desc">Price: High to Low</option>
          <option value="name">Name: A to Z</option>
        </select>
        <select
  value={size}
  onChange={(e) => {
    setSize(Number(e.target.value));
    setPage(1);
  }}
>
  <option value={10}>10 per page</option>
  <option value={20}>20 per page</option>
  <option value={50}>50 per page</option>
</select>
      </div>

      {loading ? (
        <p>Loading products...</p>
      ) : products.length === 0 ? (
        <p>No products found.</p>
      ) : (
        <>
  <div className="product-grid">
    {products.map((product) => (
      <div className="product-card" key={product.id}>
        <h3>{product.name}</h3>
        <p>{product.category}</p>
        <h2>₹{product.price.toFixed(2)}</h2>
      </div>
    ))}
  </div>

  <div className="pagination">
    <button
      disabled={page === 1}
      onClick={() => setPage(page - 1)}
    >
      Previous
    </button>

    <span>
      Page {page} of {totalPages} | Total products: {total}
    </span>

    <button
      disabled={page >= totalPages}
      onClick={() => setPage(page + 1)}
    >
      Next
    </button>
  </div>
</>
      )}
    </div>
  );
}

export default App;
