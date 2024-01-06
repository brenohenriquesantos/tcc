//DOOM
const cpf = document.querySelector("#cpf");
const senha = document.querySelector("#senha");
const btnLogin = document.querySelector("#btnLogin");
const erroMsg = document.querySelector("#erroMsg");


//eventos

btnLogin.addEventListener('click', async () => {

	event.preventDefault();

	const cpfValor = cpf.value;
	const senhaValor = senha.value;

	const credenciais = {
		cpf: cpfValor,
		senha: senhaValor
	};



	const response = await fetch('/login', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(credenciais)
	});


	if (!response.ok) {
		const txtErro = await response.text();

		erroMsg.textContent = txtErro;

		erroMsg.style.display = 'block';
	}

	const dados = await response.json();

	sessionStorage.setItem("isAdm", dados.usrAdm);

	window.location.href = '/';
})
