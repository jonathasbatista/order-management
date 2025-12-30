const API_URL = 'http://localhost:8080';
let currentId = null;

document.addEventListener('DOMContentLoaded', () => {
    loadList();
    const params = new URLSearchParams(window.location.search);
    const urlId = params.get('id');
    if (urlId) {
        selectOrder(urlId);
    }
});

const formatCurrency = (val) => (val / 100).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

async function loadList() {
    try {
        const res = await fetch(`${API_URL}/orders`);
        const list = await res.json();

        list.sort((a, b) => b.id - a.id);

        document.getElementById('ordersList').innerHTML = list.map(o => `
            <div onclick="selectOrder(${o.id})" class="p-4 mb-2 rounded-xl cursor-pointer bg-white border border-gray-100 hover:border-black hover:shadow-md transition-all">
                <div class="flex justify-between items-center">
                    <span class="font-bold text-lg">#${o.id}</span>
                    <span class="text-[10px] px-2 py-1 rounded font-bold uppercase ${getStatusColor(o.status)}">${o.status}</span>
                </div>
                <div class="text-xs text-gray-400 mt-2 font-medium">CLIENTE ID: ${o.customerId}</div>
            </div>
        `).join('');
    } catch(e) { console.error(e); }
}

function getStatusColor(status) {
    if (status === 'PAID') return 'bg-black text-white';
    if (status === 'CANCELED') return 'bg-red-100 text-red-500 line-through';
    return 'bg-gray-100 text-gray-600';
}

async function selectOrder(id) {
  if (!id) return;

  currentId = id;

  document.getElementById('emptyState')?.classList.add('hidden');
  document.getElementById('detailCard').classList.remove('hidden');

  document.getElementById('orderId').innerText = '#';
  document.getElementById('orderCustomer').innerText = '';
  document.getElementById('orderStatus').innerText = '';
  document.getElementById('orderItems').innerHTML = '';
  document.getElementById('orderTotal').innerText = formatCurrency(0);
  document.getElementById('paymentArea').classList.add('hidden');

  try {
    const res = await fetch(`${API_URL}/orders/${id}`);
    if (!res.ok) throw new Error();

    const data = await res.json();

    document.getElementById('orderId').innerText = `#${data.id}`;
    document.getElementById('orderCustomer').innerText = data.customerId ?? '—';

    const badge = document.getElementById('orderStatus');
    badge.innerText = data.status;
    badge.className = `px-4 py-2 rounded-full text-xs font-bold uppercase border ${getStatusColor(data.status)}`;

    const tbody = document.getElementById('orderItems');

    if (Array.isArray(data.items) && data.items.length) {
      tbody.innerHTML = data.items.map(i => `
        <tr class="border-b last:border-0">
          <td class="py-3">${i.productId}</td>
          <td class="py-3 text-center">${i.quantity}</td>
          <td class="py-3 text-right font-bold">
            ${formatCurrency(i.unitPriceCents * i.quantity)}
          </td>
        </tr>
      `).join('');
    } else {
      tbody.innerHTML = `
        <tr>
          <td colspan="3" class="text-center py-6 text-gray-400">
            Sem itens
          </td>
        </tr>
      `;
    }

    document.getElementById('orderTotal').innerText = formatCurrency(data.totalCents);

    if (data.status === 'NEW') {
      document.getElementById('paymentArea').classList.remove('hidden');
    }
  } catch {
    document.getElementById('orderItems').innerHTML = `
      <tr>
        <td colspan="3" class="text-center py-6 text-red-400">
          Erro ao carregar pedido
        </td>
      </tr>
    `;
  }
}

async function pay() {
    const val = document.getElementById('payAmount').value;
    if(!val) return alert("Digite um valor para pagar.");

    const body = {
        orderId: currentId,
        amountCents: Math.round(parseFloat(val) * 100),
        method: "CREDIT_CARD"
    };

    try {
        const res = await fetch(`${API_URL}/payments`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(body)
        });

        if(res.ok) {
            alert("Pagamento registrado com sucesso!");
            selectOrder(currentId);
            loadList();
        } else {
            const err = await res.json();
            alert("Erro: " + (err.message || "Falha no pagamento"));
        }
    } catch(e) { console.error(e); }
}