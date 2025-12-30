const API_URL = 'http://localhost:8080';
let cart = [];

document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    document.getElementById('searchInput').addEventListener('input', loadProducts);
    document.getElementById('categoryFilter').addEventListener('change', loadProducts);
    document.getElementById('activeToggle').addEventListener('change', loadProducts);
});

const formatCurrency = (val) => (val / 100).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

async function loadProducts() {
    const term = document.getElementById('searchInput').value.toLowerCase();
    const cat = document.getElementById('categoryFilter').value;
    const isActive = document.getElementById('activeToggle').checked;

    try {
        const res = await fetch(`${API_URL}/products`);
        let products = await res.json();

        products = products.filter(p => p.active === isActive);

        if (cat) {
            products = products.filter(p => p.category === cat);
        }

        if (term) {
            products = products.filter(p => p.name.toLowerCase().includes(term));
        }

        renderGrid(products);

        if (document.getElementById('categoryFilter').options.length <= 1) {
            setupCategories(products);
        }
    } catch (e) {
        console.error(e);
    }
}

function setupCategories(products) {
    const cats = [...new Set(products.map(p => p.category).filter(Boolean))];
    const sel = document.getElementById('categoryFilter');
    sel.innerHTML = '<option value="">Todas as Categorias</option>';
    cats.forEach(c => {
        const opt = document.createElement('option');
        opt.value = c;
        opt.innerText = c;
        sel.appendChild(opt);
    });
}

function renderGrid(products) {
    const grid = document.getElementById('productsGrid');

    if(products.length === 0) {
        const statusMsg = document.getElementById('activeToggle').checked ? "ativos" : "inativos";
        grid.innerHTML = `<p class="col-span-full text-center text-gray-400 py-10">Nenhum produto ${statusMsg} encontrado.</p>`;
        return;
    }

    grid.innerHTML = products.map(p => `
        <div class="bg-white p-6 rounded-3xl border border-gray-100 hover:border-black/10 hover:shadow-xl transition-all duration-300 group flex flex-col ${!p.active ? 'bg-gray-50' : ''}">
            <div class="flex justify-between items-start mb-4">
                <span class="text-[10px] font-bold bg-gray-100 text-gray-500 px-3 py-1 rounded-full uppercase tracking-wider">${p.category || 'GERAL'}</span>

                ${!p.active
                    ? '<span class="text-[10px] font-bold border border-red-200 bg-red-100 text-red-600 px-2 py-1 rounded-full">INATIVO</span>'
                    : '<span class="text-[10px] font-bold border border-green-200 bg-green-50 text-green-600 px-2 py-1 rounded-full">ATIVO</span>'}
            </div>

            <h3 class="font-bold text-lg mb-2 text-gray-900 ${!p.active ? 'text-gray-500' : ''}">${p.name}</h3>

            <div class="mt-auto pt-4 flex justify-between items-center">
                <span class="text-xl font-bold tracking-tight text-gray-800">${formatCurrency(p.priceCents)}</span>

                <button onclick="addToCart(${p.id}, '${p.name}', ${p.priceCents})"
                    ${!p.active ? 'disabled' : ''}
                    class="w-10 h-10 rounded-full flex items-center justify-center transition-transform hover:scale-110 active:scale-90
                    ${!p.active ? 'bg-gray-300 cursor-not-allowed text-gray-400' : 'bg-black text-white cursor-pointer'}">
                    +
                </button>
            </div>
        </div>
    `).join('');
}

function addToCart(id, name, price) {
    const item = cart.find(i => i.id === id);
    if (item) item.qty++;
    else cart.push({ id, name, priceCents: price, qty: 1 });
    renderCart();
}

function updateQty(id, delta) {
    const item = cart.find(i => i.id === id);
    if (!item) return;
    item.qty += delta;
    if (item.qty <= 0) cart = cart.filter(i => i.id !== id);
    renderCart();
}

function renderCart() {
    const container = document.getElementById('cartItems');
    const totalEl = document.getElementById('cartTotal');
    const countEl = document.getElementById('cartCount');

    if (cart.length === 0) {
        container.innerHTML = `<div class="py-10 text-center text-gray-300 text-sm">Carrinho vazio</div>`;
        totalEl.innerText = formatCurrency(0);
        countEl.innerText = '0';
        return;
    }

    let total = 0;
    container.innerHTML = cart.map(item => {
        total += item.priceCents * item.qty;
        return `
        <div class="flex items-center justify-between p-3 border-b border-gray-50 last:border-0">
            <div>
                <h4 class="text-sm font-bold text-gray-900">${item.name}</h4>
                <p class="text-xs text-gray-500">${formatCurrency(item.priceCents)} x ${item.qty}</p>
            </div>
            <div class="flex items-center gap-2">
                <button onclick="updateQty(${item.id}, -1)" class="w-6 h-6 bg-gray-100 rounded-full font-bold flex items-center justify-center">-</button>
                <button onclick="updateQty(${item.id}, 1)" class="w-6 h-6 bg-black text-white rounded-full font-bold flex items-center justify-center">+</button>
            </div>
        </div>
        `;
    }).join('');

    totalEl.innerText = formatCurrency(total);
    countEl.innerText = cart.reduce((a, b) => a + b.qty, 0);
}

async function checkout() {
    const cid = document.getElementById('customerId').value;
    if (!cid || cart.length === 0) return alert("Preencha o ID do Cliente e adicione itens.");

    const body = {
        customerId: parseInt(cid),
        items: cart.map(i => ({ productId: i.id, quantity: i.qty }))
    };

    try {
        const res = await fetch(`${API_URL}/orders`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body)
        });

        if (res.ok) {
            const order = await res.json();
            window.location.href = `orders.html?id=${order.id}`;
        } else {
            const err = await res.json();
            alert('Erro: ' + (err.message || 'Falha ao criar pedido'));
        }
    } catch (e) { console.error(e); }
}