document.addEventListener("DOMContentLoaded", () => {
  const API_BASE = "http://localhost:8080";
  
  // Elements
  const loginTab = document.getElementById("loginTab");
  const registerTab = document.getElementById("registerTab");
  const loginForm = document.getElementById("loginForm");
  const registerForm = document.getElementById("registerForm");
  const messageArea = document.getElementById("messageArea");
  
  // Buttons
  const loginButton = document.getElementById("loginButton");
  const registerButton = document.getElementById("registerButton");

  // Check if user is already logged in
  const userId = localStorage.getItem("userId");
  if (userId) {
    window.location.href = "dashboard.html";
    return;
  }

  // Tab switching
  loginTab.addEventListener("click", () => {
    switchTab("login");
  });

  registerTab.addEventListener("click", () => {
    switchTab("register");
  });

  function switchTab(tab) {
    clearMessages();
    
    if (tab === "login") {
      loginTab.classList.add("active");
      registerTab.classList.remove("active");
      loginForm.classList.add("active");
      registerForm.classList.remove("active");
    } else {
      registerTab.classList.add("active");
      loginTab.classList.remove("active");
      registerForm.classList.add("active");
      loginForm.classList.remove("active");
    }
  }

  // Message functions
  function showMessage(message, type = "error") {
    messageArea.innerHTML = `<div class="${type}-message">${message}</div>`;
  }

  function clearMessages() {
    messageArea.innerHTML = "";
  }

  // Loading state functions
  function setLoading(button, isLoading) {
    if (isLoading) {
      button.classList.add("loading");
      button.disabled = true;
      button.textContent = "Carregando...";
    } else {
      button.classList.remove("loading");
      button.disabled = false;
      button.textContent = button.id === "loginButton" ? "Entrar" : "Cadastrar";
    }
  }

  // Fetch with error handling
  async function apiRequest(url, options = {}) {
    try {
      console.log("API Request:", options.method || "GET", url, options);
      const response = await fetch(url, {
        headers: {
          "Content-Type": "application/json",
          ...options.headers
        },
        ...options
      });
      
      console.log("API Response:", response.status, response.statusText);
      
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}: ${response.statusText}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error("API Error:", error);
      throw error;
    }
  }

  // Login form handler
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    clearMessages();
    
    const username = document.getElementById("loginUsername").value.trim();
    const password = document.getElementById("loginPassword").value.trim();
    
    if (!username || !password) {
      showMessage("Por favor, preencha todos os campos.");
      return;
    }
    
    setLoading(loginButton, true);
    
    try {
      // Get all users and find matching credentials
      const users = await apiRequest(`${API_BASE}/user/list`);
      console.log("Users from API:", users);
      
      const user = users.find(u => 
        u.username === username && u.password === password
      );
      
      if (user) {
        showMessage("Login realizado com sucesso!", "success");
        localStorage.setItem("userId", String(user.id));
        
        // Redirect after short delay
        setTimeout(() => {
          window.location.href = "dashboard.html";
        }, 1000);
      } else {
        showMessage("Usuário ou senha incorretos.");
      }
      
    } catch (error) {
      console.error("Login error:", error);
      showMessage("Erro ao conectar com o servidor. Tente novamente.");
    } finally {
      setLoading(loginButton, false);
    }
  });

  // Register form handler
  registerForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    clearMessages();
    
    const username = document.getElementById("registerUsername").value.trim();
    const password = document.getElementById("registerPassword").value.trim();
    const confirmPassword = document.getElementById("confirmPassword").value.trim();
    
    // Validation
    if (!username || !password || !confirmPassword) {
      showMessage("Por favor, preencha todos os campos.");
      return;
    }
    
    if (username.length < 3) {
      showMessage("O nome de usuário deve ter pelo menos 3 caracteres.");
      return;
    }
    
    if (password.length < 4) {
      showMessage("A senha deve ter pelo menos 4 caracteres.");
      return;
    }
    
    if (password !== confirmPassword) {
      showMessage("As senhas não coincidem.");
      return;
    }
    
    setLoading(registerButton, true);
    
    try {
      // Check if username already exists
      const users = await apiRequest(`${API_BASE}/user/list`);
      const existingUser = users.find(u => u.username === username);
      
      if (existingUser) {
        showMessage("Este nome de usuário já está em uso. Escolha outro.");
        setLoading(registerButton, false);
        return;
      }
      
      // Create new user
      // Note: Adjust this endpoint based on your backend API
      const newUser = await apiRequest(`${API_BASE}/user/register`, {
        method: "POST",
        body: JSON.stringify({
          username: username,
          password: password
        })
      });
      
      showMessage("Cadastro realizado com sucesso! Você pode fazer login agora.", "success");
      
      // Clear form and switch to login tab
      registerForm.reset();
      setTimeout(() => {
        switchTab("login");
        document.getElementById("loginUsername").value = username;
        document.getElementById("loginUsername").focus();
      }, 1500);
      
    } catch (error) {
      console.error("Register error:", error);
      if (error.message.includes("404")) {
        showMessage("Endpoint de cadastro não encontrado. Verifique se o backend suporta cadastro.");
      } else {
        showMessage("Erro ao criar conta. Tente novamente.");
      }
    } finally {
      setLoading(registerButton, false);
    }
  });

  // Focus management
  document.getElementById("loginUsername").focus();
});