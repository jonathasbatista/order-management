const API_URL = 'http://localhost:8080';
let products = [];
let cart = [];

document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    document.getElementById('searchInput').addEventListener('input', renderGrid);
    document.getElementById('categoryFilter').addEventListener('change', renderGrid);
    document.getElementById('activeToggle').addEventListener('change', renderGrid);
});

const formatCurrency = (val) => (val / 100).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

async function loadProducts() {
    try {
        const res = await fetch(`${API_URL}/products`);
        products = await res.json();
        setupCategories();
        renderGrid();
    } catch (e) {
    console.error(e);
    }
}

function setupCategories() {
    const cats = [...new Set(products.map(p => p.category).filter(Boolean))];
    const sel = document.getElementById('categoryFilter');
    cats.forEach(c => {
        const opt = document.createElement('option');
        opt.value = c;
        opt.innerText = c;
        sel.appendChild(opt);
    });
}

function renderGrid() {
    const grid = document.getElementById('productsGrid');
    const term = document.getElementById('searchInput').value.toLowerCase();
    const cat = document.getElementById('categoryFilter').value;
    const activeOnly = document.getElementById('activeToggle').checked;

    const filtered = products.filter(p => {
        return p.name.toLowerCase().includes(term) &&
               (cat ? p.category === cat : true) &&
               (activeOnly ? p.active : true);
    });

    grid.innerHTML = filtered.map(p => `
        <div class="bg-white p-6 rounded-3xl border border-gray-100 hover:border-black/10 hover:shadow-xl hover:shadow-gray-100 transition-all duration-300 group flex flex-col ${!p.active ? 'opacity-40 grayscale' : ''}">
            <div class="flex justify-between items-start mb-4">
                <span class="text-[10px] font-bold bg-gray-50 text-gray-500 px-3 py-1 rounded-full uppercase tracking-wider">${p.category || 'GERAL'}</span>
                ${!p.active ? '<span class="text-[10px] font-bold border border-gray-300 px-2 py-1 rounded-full">INATIVO</span>' : ''}
            </div>

            <h3 class="font-bold text-lg mb-2 text-gray-900">${p.name}</h3>

            <div class="mt-auto pt-4 flex justify-between items-center">
                <span class="text-xl font-bold tracking-tight">${formatCurrency(p.priceCents)}</span>
                <button onclick="addToCart(${p.id})" ${!p.active ? 'disabled' : ''} class="w-10 h-10 rounded-full bg-black text-white flex items-center justify-center hover:scale-110 active:scale-90 transition-transform disabled:bg-gray-200 disabled:cursor-not-allowed">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4"></path></svg>
                </button>
            </div>
        </div>
    `).join('');
}

function addToCart(id) {
    const item = cart.find(i => i.id === id);
    if (item) item.qty++;
    else {
        const p = products.find(p => p.id === id);
        cart.push({ ...p, qty: 1 });
    }
    renderCart();
}

function updateQty(id, delta) {
    const item = cart.find(i => i.id === id);
    if (!item) {
        return;
    }

    item.qty += delta;

    if (item.qty <= 0) {
        cart = cart.filter(i => i.id !== id);
    }
    renderCart();
}

function renderCart() {
    const container = document.getElementById('cartItems');
    const totalEl = document.getElementById('cartTotal');
    const countEl = document.getElementById('cartCount');

    if (cart.length === 0) {
        container.innerHTML = `
            <div class="flex flex-col items-center justify-center py-10 text-gray-300 border-2 border-dashed border-gray-100 rounded-2xl">
                <span class="text-sm">Carrinho vazio</span>
            </div>`;
        totalEl.innerText = formatCurrency(0);
        countEl.innerText = '0';
        return;
    }

    let total = 0;
    container.innerHTML = cart.map(item => {
        total += item.priceCents * item.qty;
        return `
        <div class="flex items-center gap-4 p-3 hover:bg-gray-50 rounded-2xl transition-colors group">
            <div class="flex-1">
                <h4 class="text-sm font-bold text-gray-900">${item.name}</h4>
                <p class="text-xs text-gray-500 font-medium">${formatCurrency(item.priceCents)}</p>
            </div>
            <div class="flex items-center gap-1 bg-white border border-gray-100 rounded-full px-1 py-1 shadow-sm">
                <button onclick="updateQty(${item.id}, -1)" class="w-6 h-6 rounded-full hover:bg-gray-100 flex items-center justify-center text-xs font-bold text-gray-500 transition-colors">-</button>
                <span class="text-xs font-bold w-4 text-center select-none">${item.qty}</span>
                <button onclick="updateQty(${item.id}, 1)" class="w-6 h-6 rounded-full hover:bg-gray-100 flex items-center justify-center text-xs font-bold text-gray-500 transition-colors">+</button>
            </div>
        </div>
        `;
    }).join('');

    totalEl.innerText = formatCurrency(total);
    countEl.innerText = cart.reduce((a, b) => a + b.qty, 0);
}

async function checkout() {
    const cid = document.getElementById('customerId').value;
    if (!cid || cart.length === 0){
     return alert("Carrinho vazio ou Cliente inválido.");
    }

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
            alert('Erro ao criar pedido');
        }
    } catch (e) { console.error(e); }
}