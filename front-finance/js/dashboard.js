document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8080"; // ajustar se necessário
  const userId = localStorage.getItem("userId");

  if (!userId) {
    window.location.href = "login.html";
    return;
  }

  // DOM
  const userLabel = document.getElementById("userLabel");
  const lista = document.getElementById("listaTransacoes");
  const form = document.getElementById("transacaoForm");
  const transactionIdInput = document.getElementById("transactionId");
  const valorInput = document.getElementById("valor");
  const descricaoInput = document.getElementById("descricao");
  const typeSelect = document.getElementById("transactionType");
  const cancelEditBtn = document.getElementById("cancelEditBtn");
  const logoutBtn = document.getElementById("logoutBtn");
  const balanceAmount = document.getElementById("balanceAmount");
  const refreshBalanceBtn = document.getElementById("refreshBalanceBtn");

  userLabel.textContent = `Usuário ID: ${userId}`;

  // Fetch com logs (usa clone para não consumir o body original)
  async function doFetch(url, options = {}) {
    console.log("FETCH ->", options.method || "GET", url, options);
    const res = await fetch(url, options);
    // clona para ler o body para log sem impedir leitura posterior
    try {
      const clone = res.clone();
      const text = await clone.text();
      console.log("RESPONSE status:", res.status, res.statusText);
      console.log("RESPONSE body:", text);
    } catch (e) {
      console.log("RESPONSE body: (erro ao ler)");
    }
    return res;
  }

  // Carregar saldo total
  async function carregarSaldo() {
    try {
      const res = await doFetch(`${API_BASE}/${userId}/total`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const saldo = await res.json();
      
      // Formatar e exibir o saldo
      const valorFormatado = Number(saldo).toLocaleString('pt-BR', {
        style: 'currency',
        currency: 'BRL'
      });
      
      balanceAmount.textContent = valorFormatado;
      
      // Aplicar classe CSS baseada no valor
      balanceAmount.className = 'balance-amount';
      if (saldo > 0) {
        balanceAmount.classList.add('balance-positive');
      } else if (saldo < 0) {
        balanceAmount.classList.add('balance-negative');
      } else {
        balanceAmount.classList.add('balance-zero');
      }
      
    } catch (err) {
      console.error('Erro ao carregar saldo:', err);
      balanceAmount.textContent = 'Erro ao carregar';
      balanceAmount.className = 'balance-amount balance-negative';
    }
  }
  // Carregar transações (paginado: dados.content)
  async function carregarTransacoes() {
    try {
      const res = await doFetch(`${API_BASE}/${userId}/list`);
      if (!res.ok) throw new Error(`HTTP ${res.status}`);
      const dados = await res.json();
      const listaDados = Array.isArray(dados) ? dados : (dados.content || []);
      renderLista(listaDados);
    } catch (err) {
      console.error(err);
      lista.innerHTML = "<li>Erro ao carregar transações</li>";
    }
  }

  function renderLista(dados) {
    lista.innerHTML = "";
    if (!Array.isArray(dados) || dados.length === 0) {
      lista.innerHTML = `
        <li class="empty-state">
          <div>Nenhuma transação encontrada.</div>
        </li>
      `;
      return;
    }

    dados.forEach(t => {
      const li = document.createElement("li");
      const transactionTypeClass = t.transactionType === "INPUT" ? "transaction-input" : "transaction-output";
      const transactionTypeText = t.transactionType === "INPUT" ? "ENTRADA" : "SAÍDA";
      
      li.innerHTML = `
        <div>
          <div class="transaction-info">
            <strong class="${transactionTypeClass}">${transactionTypeText}</strong>
            <div class="transaction-amount">R$ ${Number(t.value ?? 0).toFixed(2)}</div>
            ${t.description ? `<div class="transaction-description">${escapeHtml(t.description)}</div>` : ""}
          </div>
          <div class="transaction-actions">
            <button class="editBtn"
              data-id="${t.id}"
              data-value="${t.value}"
              data-transactiontype="${t.transactionType}"
              data-description="${t.description ?? ""}"
              title="Editar transação">
              ✏️ Editar
            </button>
            <button class="delBtn" data-id="${t.id}" title="Excluir transação">
              🗑️ Excluir
            </button>
          </div>
        </div>
      `;
      lista.appendChild(li);
    });

    // eventos
    document.querySelectorAll(".editBtn").forEach(btn => {
      btn.addEventListener("click", (e) => {
        const id = e.currentTarget.dataset.id;
        const value = e.currentTarget.dataset.value;
        const transactionType = e.currentTarget.dataset.transactiontype;
        const description = e.currentTarget.dataset.description;
        
        console.log("Editando transação:", { id, value, transactionType, description });
        
        transactionIdInput.value = id;
        valorInput.value = value || "";
        typeSelect.value = transactionType || "";
        descricaoInput.value = description || "";
        cancelEditBtn.style.display = "inline-block";
        
        // Adicionar indicação visual de modo edição
        form.scrollIntoView({ behavior: 'smooth' });
        
        // Destacar o formulário visualmente
        form.style.border = "3px solid #007bff";
        form.style.background = "#e3f2fd";
        setTimeout(() => {
          form.style.border = "";
          form.style.background = "";
        }, 2000);
      });
    });

    document.querySelectorAll(".delBtn").forEach(btn => {
      btn.addEventListener("click", async (e) => {
        const id = e.currentTarget.dataset.id;
        if (!confirm("Confirma exclusão?")) return;
        try {
          const res = await doFetch(`${API_BASE}/${userId}/${id}`, { method: "DELETE" });
          if (!res.ok) throw new Error(`HTTP ${res.status}`);
          carregarTransacoes();
          carregarSaldo(); // Atualizar saldo após excluir
        } catch (err) {
          console.error(err);
          alert("Erro ao excluir transação (veja console).");
        }
      });
    });
  }

  // Submit do form -> POST (com io) ou PUT
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = transactionIdInput.value;
    const valor = parseFloat(valorInput.value);
    const transactionType = typeSelect.value;
    const descricao = descricaoInput.value.trim();

    // Validações
    if (!transactionType) {
      alert("Selecione o tipo da transação (Entrada ou Saída).");
      return;
    }

    if (isNaN(valor) || valor <= 0) {
      alert("Digite um valor válido maior que zero.");
      return;
    }

    console.log("Dados do formulário:", { id, valor, transactionType, descricao });

    try {
      let res;
      if (id) {
        // PUT para editar transação existente
        // Seu TransactionPUTDTO espera: { id: Long, value: BigDecimal }
        const bodyForController = { 
          id: parseInt(id),  // Converter para número
          value: valor
        };

        console.log("Enviando PUT:", bodyForController);

        // Endpoint PUT - sem o ID na URL, apenas /{usuarioId}
        res = await doFetch(`${API_BASE}/${userId}`, {
          method: "PUT",
          headers: { 
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
          body: JSON.stringify(bodyForController)
        });

        if (!res.ok) {
          // Tentar ler a mensagem de erro do backend
          let errorMessage = `HTTP ${res.status}`;
          try {
            const errorData = await res.text();
            if (errorData) {
              errorMessage += ` - ${errorData}`;
            }
          } catch (e) {
            // Ignorar erro ao ler response
          }
          throw new Error(errorMessage);
        }

        console.log("PUT realizado com sucesso");

      } else {
        // POST para criar nova transação
        const io = transactionType === "INPUT" ? 1 : 2;
        const bodyForController = { value: valor };
        if (descricao) bodyForController.description = descricao;

        console.log("Enviando POST:", bodyForController);

        res = await doFetch(`${API_BASE}/${userId}/${io}/launch`, {
          method: "POST",
          headers: { 
            "Content-Type": "application/json",
            "Accept": "application/json"
          },
          body: JSON.stringify(bodyForController)
        });

        if (!res.ok) {
          let errorMessage = `HTTP ${res.status}`;
          try {
            const errorData = await res.text();
            if (errorData) {
              errorMessage += ` - ${errorData}`;
            }
          } catch (e) {
            // Ignorar erro ao ler response
          }
          throw new Error(errorMessage);
        }

        console.log("POST realizado com sucesso");
      }

      // Limpar formulário e recarregar lista
      transactionIdInput.value = "";
      form.reset();
      cancelEditBtn.style.display = "none";
      
      alert(id ? "Transação atualizada com sucesso!" : "Transação criada com sucesso!");
      carregarTransacoes();
      carregarSaldo(); // Atualizar saldo após salvar

    } catch (err) {
      console.error("Erro na operação:", err);
      alert(`Erro ao ${id ? 'atualizar' : 'criar'} transação: ${err.message}`);
    }
  });

  cancelEditBtn.addEventListener("click", () => {
    transactionIdInput.value = "";
    form.reset();
    cancelEditBtn.style.display = "none";
  });

  refreshBalanceBtn.addEventListener("click", () => {
    carregarSaldo();
  });

  logoutBtn.addEventListener("click", () => {
    if (confirm("Tem certeza que deseja sair?")) {
      localStorage.removeItem("userId");
      // Limpar outros dados se necessário
      // localStorage.clear(); // Descomente se quiser limpar tudo
      
      // Feedback visual
      document.body.style.opacity = "0.5";
      
      // Redirect após breve delay
      setTimeout(() => {
        window.location.href = "login.html";
      }, 500);
    }
  });

  function escapeHtml(str) {
    return String(str || "")
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  }

  // Carregar dados iniciais
  carregarTransacoes();
  carregarSaldo();
});