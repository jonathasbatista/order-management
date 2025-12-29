const API_URL = 'http://localhost:8080';
let currentId = null;

document.addEventListener('DOMContentLoaded', () => {
    loadList();
    const params = new URLSearchParams(window.location.search);
    if(params.get('id')){
     selectOrder(params.get('id'));
    }
});

const formatCurrency = (val) => (val / 100).toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

async function loadList() {
    try {
        const res = await fetch(`${API_URL}/orders`);
        const list = await res.json();
        list.sort((a, b) => b.id - a.id);

        document.getElementById('ordersList').innerHTML = list.map(o => {
            let statusStyle = "bg-gray-100 text-gray-500 border-gray-200";
            if (o.status === 'PAID') {
                statusStyle = "bg-black text-white border-black";
            }
            if (o.status === 'CANCELED') {
                statusStyle = "bg-transparent text-gray-300 line-through border-transparent";
            }

            return `
            <div onclick="selectOrder(${o.id})" class="p-4 rounded-2xl cursor-pointer hover:bg-gray-50 transition-all border border-transparent hover:border-gray-100 group">
                <div class="flex justify-between items-center mb-1">
                    <span class="font-bold text-lg group-hover:underline decoration-2 underline-offset-4 decoration-gray-200">#${o.id}</span>
                    <span class="text-[10px] px-2 py-1 rounded-full font-bold uppercase border ${statusStyle}">${o.status}</span>
                </div>
                <div class="text-xs font-bold text-gray-400">CLIENTE ID: ${o.customerId}</div>
            </div>
            `;
        }).join('');
    } catch(e) { console.error(e); }
}

async function selectOrder(id) {
    currentId = id;
    document.getElementById('emptyState').classList.add('hidden');
    document.getElementById('detailCard').classList.remove('hidden');

    try {
        const res = await fetch(`${API_URL}/orders/${id}`);
        const data = await res.json();

        document.getElementById('orderId').innerText = `#${data.order.id}`;
        document.getElementById('orderCustomer').innerText = data.order.customerId;
        document.getElementById('orderTotal').innerText = formatCurrency(data.totalCents);

        const badge = document.getElementById('orderStatus');
        badge.innerText = data.order.status;

        badge.className = "px-4 py-2 rounded-full text-xs font-bold tracking-widest border uppercase ";
        if(data.order.status === 'PAID'){
            badge.className += "bg-black text-white border-black shadow-lg shadow-gray-200";
        }
        else if(data.order.status === 'CANCELED'){
            badge.className += "bg-gray-50 text-gray-400 border-gray-100 line-through";
        }
        else{
            badge.className += "bg-white text-black border-gray-200";
        }

        document.getElementById('orderItems').innerHTML = data.items.map(i => `
            <tr class="border-b border-gray-50 last:border-0 hover:bg-gray-50 transition-colors">
                <td class="py-4 pl-2 rounded-l-lg font-bold text-gray-900">Prod ${i.productId}</td>
                <td class="py-4 text-right text-gray-500">${i.quantity}</td>
                <td class="py-4 text-right pr-2 rounded-r-lg font-bold">${formatCurrency(i.unitPriceCents * i.quantity)}</td>
            </tr>
        `).join('');

        const payArea = document.getElementById('paymentArea');
        if(data.order.status !== 'PAID' && data.order.status !== 'CANCELED') {
            payArea.classList.remove('hidden');
            document.getElementById('payAmount').value = '';
        } else {
            payArea.classList.add('hidden');
        }

    } catch(e) { console.error(e); }
}

async function pay() {
    const val = document.getElementById('payAmount').value;
    if(!val){
        return;
    }

    try {
        const res = await fetch(`${API_URL}/payments`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ orderId: currentId, amountCents: Math.round(val * 100) })
        });
        if(res.ok) {
            selectOrder(currentId);
            loadList();
        }
    } catch(e) { console.error(e); }
}