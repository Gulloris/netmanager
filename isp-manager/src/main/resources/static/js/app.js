// Modal de dar baixa em fatura
function abrirModalBaixa(btn) {
    const faturaId = btn.getAttribute('data-id');
    const form = document.getElementById('formBaixa');
    form.action = `/faturas/${faturaId}/baixa`;

    // Preenche data de hoje por padrão
    const hoje = new Date().toISOString().split('T')[0];
    document.getElementById('inputDataPgto').value = hoje;

    document.getElementById('modalBaixa').style.display = 'flex';
}

function fecharModal() {
    document.getElementById('modalBaixa').style.display = 'none';
}

// Fechar modal clicando fora
document.addEventListener('DOMContentLoaded', () => {
    const overlay = document.getElementById('modalBaixa');
    if (overlay) {
        overlay.addEventListener('click', (e) => {
            if (e.target === overlay) fecharModal();
        });
    }

    // Auto-dismiss alertas após 4 segundos
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity .4s';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 400);
        }, 4000);
    });

    // Máscara CPF simples
    const cpfInput = document.querySelector('input[name="cpf"]');
    if (cpfInput) {
        cpfInput.addEventListener('input', function () {
            let v = this.value.replace(/\D/g, '');
            if (v.length <= 11) {
                v = v.replace(/(\d{3})(\d)/, '$1.$2')
                     .replace(/(\d{3})(\d)/, '$1.$2')
                     .replace(/(\d{3})(\d{1,2})$/, '$1-$2');
            }
            this.value = v;
        });
    }

    // Máscara telefone
    const telInput = document.querySelector('input[name="telefone"]');
    if (telInput) {
        telInput.addEventListener('input', function () {
            let v = this.value.replace(/\D/g, '');
            v = v.replace(/^(\d{2})(\d)/, '($1) $2')
                 .replace(/(\d{5})(\d)/, '$1-$2');
            this.value = v;
        });
    }
});
