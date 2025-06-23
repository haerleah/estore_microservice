import sectionConfig from './config.js';

let currentSection = 'electro';
let currentPage = 0;
let pageSize = 10;
let sortField = 'id';
let sortDir = 'asc';

const tableHeader = document.getElementById('table-header');
const tableBody = document.getElementById('table-body');
const prevBtn = document.getElementById('prev-page');
const nextBtn = document.getElementById('next-page');
const pageInfo = document.getElementById('page-info');
const pageSizeInput = document.getElementById('page-size-input');
const addBtn = document.getElementById('add-btn');
const fileInput = document.getElementById('zip-file');
const uploadBtn = document.getElementById('upload-btn');
const fileNameLabel = document.getElementById('file-name');
const toggleButtons = document.querySelectorAll('.toggle');
const controlsContainer = document.querySelector('.controls');
const tableContainer = document.querySelector('.table-container');
const numericFields = ['price'];

function hideMainControls() {
    controlsContainer.style.display = 'none';
    tableContainer.style.display = 'none';
    prevBtn.style.display = 'none';
    nextBtn.style.display = 'none';
    pageInfo.style.display = 'none';
}

function showMainControls() {
    controlsContainer.style.display = '';
    tableContainer.style.display = '';
    prevBtn.style.display = '';
    nextBtn.style.display = '';
    pageInfo.style.display = '';
}

function initToggles() {
    toggleButtons.forEach(btn =>
        btn.addEventListener('click', e => {
            e.preventDefault();
            const sub = document.getElementById(btn.dataset.target);
            sub.style.display = sub.style.display === 'block' ? 'none' : 'block';
        })
    );
}

function initSectionLinks() {
    document.querySelectorAll('.menu a[data-section]').forEach(link =>
        link.addEventListener('click', async e => {
            e.preventDefault();
            currentSection = link.dataset.section;
            currentPage = 0;
            sortField = 'id';
            sortDir = 'asc';
            document.getElementById('create-form-container').innerHTML = '';
            if (currentSection === 'bestCriteria') {
                await showBestForm();
            } else if (currentSection === 'bestByPosition') {
                await showBestByPositionForm();
            } else if (currentSection === 'sumByPayment') {
                await showSumByPaymentForm();
            } else {
                loadData();
            }
        })
    );
}

function initPagination() {
    prevBtn.addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            loadData();
        }
    });
    nextBtn.addEventListener('click', () => {
        currentPage++;
        loadData();
    });
    pageSizeInput.addEventListener('change', () => {
        const v = parseInt(pageSizeInput.value, 10);
        if (v >= 1) {
            pageSize = v;
            currentPage = 0;
            loadData();
        } else {
            pageSizeInput.value = pageSize;
        }
    });
}

function initUpload() {
    fileInput.addEventListener('change', () => {
        if (fileInput.files.length) {
            fileNameLabel.textContent = fileInput.files[0].name;
            uploadBtn.hidden = false;
        } else {
            fileNameLabel.textContent = 'Файл не выбран';
            uploadBtn.hidden = true;
        }
    });
    uploadBtn.addEventListener('click', async () => {
        const files = fileInput.files;
        if (!files || files.length === 0) {
            alert('Пожалуйста, выберите ZIP-файл');
            return;
        }
        const zip = files[0];
        const fd = new FormData();
        fd.append('file', zip, zip.name);
        try {
            const res = await fetch('/estore/api/import/all', {method: 'POST', body: fd});
            if (!res.ok) {
                const data = await res.json();
                throw new Error(data.message);
            }
            alert('Архив успешно загружен');
            loadData();
        } catch (e) {
            alert('Ошибка загрузки: ' + e.message);
        } finally {
            fileInput.value = '';
            fileNameLabel.textContent = 'Файл не выбран';
            uploadBtn.hidden = true;
        }
    });
}

function formatValue(key, value) {
    if (typeof value === 'boolean') {
        if (key === 'gender') return value ? 'мужской' : 'женский';
        return value ? 'да' : 'нет';
    }
    return value ?? '';
}

async function fetchJSON(url) {
    const res = await fetch(url);
    return res.json();
}

async function populateSelect(select, endpoint, nameField, addEmpty = false, emptyText = '') {
    if (addEmpty) {
        const option = document.createElement('option');
        option.value = '';
        option.textContent = emptyText;
        select.appendChild(option);
    }
    const data = await fetchJSON(endpoint);
    const list = Array.isArray(data.content) ? data.content : data;
    list.forEach(item => {
        const option = document.createElement('option');
        option.value = item.id;
        option.textContent = typeof nameField === 'function'
            ? nameField(item)
            : item[nameField];
        select.appendChild(option);
    });
}

function renderTable(items) {
    showMainControls();
    const cfg = sectionConfig[currentSection];
    tableHeader.innerHTML = '';
    cfg.fields.forEach(f => {
        const th = document.createElement('th');
        th.textContent = cfg.labels[f];
        th.dataset.field = f;
        const arrow = document.createElement('span');
        arrow.className = 'sort-arrow';
        if (sortField === f) arrow.textContent = sortDir === 'asc' ? '▲' : '▼';
        th.appendChild(arrow);
        th.addEventListener('click', () => {
            if (sortField === f) sortDir = sortDir === 'asc' ? 'desc' : 'asc';
            else {
                sortField = f;
                sortDir = 'asc';
            }
            currentPage = 0;
            loadData();
        });
        tableHeader.appendChild(th);
    });
    tableBody.innerHTML = items
        .map(item => {
            const row = cfg.fields.map(f => `<td>${formatValue(f, item[f])}</td>`).join('');
            return `<tr>${row}</tr>`;
        })
        .join('');
    tableBody.querySelectorAll('tr').forEach((tr, i) => {
        tr.style.cursor = 'pointer';
        tr.addEventListener('click', () => showForm(items[i]));
    });
}

function loadData() {
    showMainControls();
    const cfg = sectionConfig[currentSection];
    const url = new URL(cfg.endpoint, window.location.origin);
    url.searchParams.set('page', currentPage);
    url.searchParams.set('size', pageSize);
    url.searchParams.set('sort', `${sortField},${sortDir}`);
    fetch(url)
        .then(r => r.json())
        .then(data => {
            const items = Array.isArray(data.content) ? data.content : data;
            renderTable(items);
            const total = data.totalPages ?? 1;
            pageInfo.textContent = `Стр. ${currentPage + 1} / ${total}`;
            prevBtn.disabled = currentPage <= 0;
            nextBtn.disabled = currentPage >= total - 1;
        })
        .catch(() => {
            tableBody.innerHTML = '<tr><td colspan="100%">Ошибка загрузки</td></tr>';
        });
}

async function showBestForm() {
    hideMainControls();
    const container = document.getElementById('create-form-container');
    container.innerHTML = '';
    const form = document.createElement('form');
    form.style.margin = '12px';
    const h2 = document.createElement('h2');
    h2.textContent = 'Лучшие сотрудники по критериям';
    form.appendChild(h2);
    const yearLimWr = document.createElement('div');
    yearLimWr.style.marginBottom = '8px';
    const limWr = document.createElement('div');
    limWr.style.marginBottom = '8px';
    const yearLimLbl = document.createElement('label');
    yearLimLbl.textContent = 'За сколько лет произвести выборку:';
    const limLbl = document.createElement('label');
    limLbl.textContent = 'Максимум сотрудников:';
    const limInp = document.createElement('input');
    limInp.type = 'number';
    limInp.min = '1';
    limInp.value = '10';
    limInp.required = true;
    limInp.style.width = '60px';
    const yearLimInp = document.createElement('input');
    yearLimInp.type = 'number';
    yearLimInp.min = '1';
    yearLimInp.value = '10';
    yearLimInp.required = true;
    yearLimInp.style.width = '60px';
    limLbl.appendChild(limInp);
    limWr.appendChild(limLbl);
    yearLimLbl.appendChild(yearLimInp);
    yearLimWr.appendChild(yearLimLbl);
    form.appendChild(limWr);
    form.appendChild(yearLimWr);
    const posWr = document.createElement('div');
    posWr.style.marginBottom = '8px';
    const posLbl = document.createElement('label');
    posLbl.textContent = 'Должность:';
    const posSel = document.createElement('select');
    posSel.required = true;
    posSel.style.width = '100%';
    posLbl.appendChild(posSel);
    posWr.appendChild(posLbl);
    form.appendChild(posWr);
    await populateSelect(posSel, '/estore/api/position?page=0&size=1000', 'name');
    const metWr = document.createElement('div');
    metWr.style.marginBottom = '8px';
    const metTitle = document.createElement('div');
    metTitle.textContent = 'Метрики (хотя бы одна):';
    metWr.appendChild(metTitle);
    ['soldCount', 'soldSum'].forEach(m => {
        const lab = document.createElement('label');
        lab.style.display = 'block';
        const chk = document.createElement('input');
        chk.type = 'checkbox';
        chk.name = 'metrics';
        chk.value = m;
        lab.appendChild(chk);
        lab.appendChild(
            document.createTextNode(' ' + (m === 'soldCount' ? 'Количество продаж' : 'Сумма продаж'))
        );
        metWr.appendChild(lab);
    });
    form.appendChild(metWr);
    const btnWr = document.createElement('div');
    btnWr.style.marginTop = '12px';
    const btnOk = document.createElement('button');
    btnOk.type = 'submit';
    btnOk.textContent = 'Показать';
    btnOk.className = 'btn';
    btnWr.appendChild(btnOk);
    form.appendChild(btnWr);
    container.appendChild(form);
    form.addEventListener('submit', async e => {
        e.preventDefault();
        const mets = Array.from(form.querySelectorAll('input[name="metrics"]:checked')).map(c => c.value);
        if (!mets.length) {
            alert('Выберите хотя бы одну метрику');
            return;
        }
        const params = new URLSearchParams();
        params.set('positionId', posSel.value);
        mets.forEach(m => params.append('metrics', m));
        params.set('limit', limInp.value);
        params.set('year', yearLimInp.value);
        const res = await fetch('/estore/api/employee/best?' + params.toString());
        if (!res.ok) {
            const data = await res.json();
            alert('Error: ' + data.message);
            return;
        }
        const list = await res.json();
        const thEl = document.getElementById('table-header');
        const tbEl = document.getElementById('table-body');
        thEl.innerHTML = '';
        tbEl.innerHTML = '';
        ['ID', 'ФИО', 'Сумма продаж', 'Кол-во продаж'].forEach(t => {
            const th = document.createElement('th');
            th.textContent = t;
            thEl.appendChild(th);
        });
        list.forEach(emp => {
            const tr = document.createElement('tr');
            [emp.id, emp.name, emp.soldSum, emp.soldCount].forEach(v => {
                const td = document.createElement('td');
                td.textContent = v;
                tr.appendChild(td);
            });
            tbEl.appendChild(tr);
        });
        showMainControls();
    });
}

async function showBestByPositionForm() {
    hideMainControls();
    const container = document.getElementById('create-form-container');
    container.innerHTML = '';
    const form = document.createElement('form');
    form.style.margin = '12px';
    const h2 = document.createElement('h2');
    h2.textContent = 'Лучший сотрудник по должности';
    form.appendChild(h2);
    const posWr = document.createElement('div');
    posWr.style.marginBottom = '8px';
    const posLbl = document.createElement('label');
    posLbl.textContent = 'Должность:';
    const posSel = document.createElement('select');
    posSel.required = true;
    posSel.style.width = '100%';
    posLbl.appendChild(posSel);
    posWr.appendChild(posLbl);
    form.appendChild(posWr);
    await populateSelect(posSel, '/estore/api/position?page=0&size=1000', 'name');
    const typeWr = document.createElement('div');
    typeWr.style.marginBottom = '8px';
    const typeLbl = document.createElement('label');
    typeLbl.textContent = 'Тип товара (необязательно):';
    const typeSel = document.createElement('select');
    typeSel.style.width = '100%';
    typeLbl.appendChild(typeSel);
    typeWr.appendChild(typeLbl);
    form.appendChild(typeWr);
    await populateSelect(typeSel, '/estore/api/item-type?page=0&size=1000', 'name', true, '—необязательно—');
    const btnWr = document.createElement('div');
    btnWr.style.marginTop = '12px';
    const btnOk = document.createElement('button');
    btnOk.type = 'submit';
    btnOk.textContent = 'Показать';
    btnOk.className = 'btn';
    btnWr.appendChild(btnOk);
    form.appendChild(btnWr);
    container.appendChild(form);
    form.addEventListener('submit', async e => {
        e.preventDefault();
        let url = `/estore/api/position/${posSel.value}/employees/best`;
        if (typeSel.value) url += `?itemTypeId=${typeSel.value}`;
        const res = await fetch(url);
        if (!res.ok) {
            const data = await res.json();
            alert('Error: ' + data.message);
            return;
        }
        const emp = await res.json();
        const thEl = document.getElementById('table-header');
        const tbEl = document.getElementById('table-body');
        thEl.innerHTML = '';
        tbEl.innerHTML = '';
        ['ID', 'ФИО', 'Должность', 'Магазин', 'Кол-во продаж'].forEach(t => {
            const th = document.createElement('th');
            th.textContent = t;
            thEl.appendChild(th);
        });
        const tr = document.createElement('tr');
        [emp.id, emp.name, emp.position, emp.shopName, emp.soldCount].forEach(v => {
            const td = document.createElement('td');
            td.textContent = v;
            tr.appendChild(td);
        });
        tbEl.appendChild(tr);
        showMainControls();
    });
}

async function showSumByPaymentForm() {
    hideMainControls();
    const container = document.getElementById('create-form-container');
    container.innerHTML = '';
    const form = document.createElement('form');
    form.style.margin = '12px';
    const h2 = document.createElement('h2');
    h2.textContent = 'Сумма оплат по способу';
    form.appendChild(h2);
    const sw = document.createElement('div');
    sw.style.marginBottom = '8px';
    const sl = document.createElement('label');
    sl.textContent = 'Магазин:';
    const ss = document.createElement('select');
    ss.required = true;
    ss.style.width = '100%';
    sl.appendChild(ss);
    sw.appendChild(sl);
    form.appendChild(sw);
    await populateSelect(ss, '/estore/api/shop?page=0&size=1000', 'name');
    const pw = document.createElement('div');
    pw.style.marginBottom = '8px';
    const pl = document.createElement('label');
    pl.textContent = 'Способ оплаты:';
    const ps = document.createElement('select');
    ps.required = true;
    ps.style.width = '100%';
    pl.appendChild(ps);
    pw.appendChild(pl);
    form.appendChild(pw);
    await populateSelect(ps, '/estore/api/purchase-type?page=0&size=1000', 'name');
    const bw = document.createElement('div');
    bw.style.marginTop = '12px';
    const btnOk = document.createElement('button');
    btnOk.type = 'submit';
    btnOk.textContent = 'Показать';
    btnOk.className = 'btn';
    bw.appendChild(btnOk);
    form.appendChild(bw);
    container.appendChild(form);
    form.addEventListener('submit', async e => {
        e.preventDefault();
        const res = await fetch(`/estore/api/shop/${ss.value}/payments/${ps.value}/total`);
        if (!res.ok) return;
        const dto = await res.json();
        const thEl = document.getElementById('table-header');
        const tbEl = document.getElementById('table-body');
        thEl.innerHTML = '';
        tbEl.innerHTML = '';
        ['ID магазина', 'Способ оплаты', 'Сумма оплат'].forEach(t => {
            const th = document.createElement('th');
            th.textContent = t;
            thEl.appendChild(th);
        });
        const tr = document.createElement('tr');
        [dto.shopId, dto.purchaseTypeName, dto.totalAmount].forEach(v => {
            const td = document.createElement('td');
            td.textContent = v;
            tr.appendChild(td);
        });
        tbEl.appendChild(tr);
        showMainControls();
    });
}

async function showForm(item = null) {
    hideMainControls();
    const cfg = sectionConfig[currentSection];
    const container = document.getElementById('create-form-container');
    container.innerHTML = '';
    const form = document.createElement('form');
    form.style.margin = '12px';
    const h2 = document.createElement('h2');
    h2.textContent = item ? 'Редактирование' : 'Создание';
    form.appendChild(h2);
    let stocksWrapper, addStockBtn, allShops = [], totalShops = 0;
    const archiveState = {checked: !!(item && item.archive)};
    if (cfg.createFields.includes('stocks')) {
        const sd = await fetchJSON(`${cfg.refs.shopId.endpoint}?page=0&size=1000`);
        allShops = Array.isArray(sd.content) ? sd.content : sd;
        totalShops = allShops.length;
    }

    async function renderStockRow(selId = null, cnt = '') {
        const row = document.createElement('div');
        row.style.display = 'flex';
        row.style.alignItems = 'center';
        row.style.gap = '8px';
        row.style.marginBottom = '4px';
        const sel = document.createElement('select');
        sel.name = 'stocks.shopId';
        sel.required = true;
        sel.style.flex = '1';
        const chosen = Array.from(stocksWrapper.querySelectorAll('select')).map(s => s.value);
        allShops.forEach(s => {
            if (!chosen.includes(String(s.id)) || s.id === Number(selId)) {
                const o = document.createElement('option');
                o.value = s.id;
                o.textContent = s[cfg.refs.shopId.nameField];
                if (s.id === Number(selId)) o.selected = true;
                sel.appendChild(o);
            }
        });
        row.appendChild(sel);
        const inp = document.createElement('input');
        inp.type = 'number';
        inp.name = 'stocks.count';
        inp.min = '0';
        inp.value = cnt;
        inp.required = true;
        inp.style.width = '80px';
        row.appendChild(inp);
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.textContent = '–';
        btn.className = 'btn';
        btn.addEventListener('click', () => {
            row.remove();
            updateStockControls();
        });
        row.appendChild(btn);
        stocksWrapper.appendChild(row);
    }

    function updateStockControls() {
        if (!stocksWrapper || !addStockBtn) return;
        if (archiveState.checked) {
            stocksWrapper.style.display = 'none';
            addStockBtn.style.display = 'none';
            stocksWrapper.innerHTML = '';
            return;
        }
        stocksWrapper.style.display = '';
        addStockBtn.style.display = '';
        const rows = stocksWrapper.querySelectorAll('div');
        if (rows.length === 0) {
            renderStockRow();
            return;
        }
        rows.forEach(r => {
            const b = r.querySelector('button');
            if (b) b.disabled = rows.length <= 1;
        });
        addStockBtn.disabled = rows.length >= totalShops;
    }

    for (const field of cfg.createFields) {
        const wr = document.createElement('div');
        wr.style.marginBottom = '8px';
        const lbl = document.createElement('label');
        lbl.textContent = cfg.labels[field.replace(/Id$/, 'Name')];
        lbl.htmlFor = field;
        lbl.style.display = 'block';
        wr.appendChild(lbl);
        if (field === 'stocks') {
            stocksWrapper = document.createElement('div');
            stocksWrapper.style.marginBottom = '12px';
            stocksWrapper.style.maxHeight = '200px';
            stocksWrapper.style.overflowY = 'auto';
            wr.appendChild(stocksWrapper);
            addStockBtn = document.createElement('button');
            addStockBtn.type = 'button';
            addStockBtn.textContent = '+ Добавить магазин';
            addStockBtn.className = 'btn';
            addStockBtn.addEventListener('click', () => renderStockRow());
            wr.appendChild(addStockBtn);
            if (item && Array.isArray(item.stocks)) {
                for (const s of item.stocks) await renderStockRow(s.shopId, s.count);
            } else {
                await renderStockRow();
            }
            updateStockControls();
        } else if (field === 'archive') {
            const hid = document.createElement('input');
            hid.type = 'hidden';
            hid.name = 'archive';
            hid.value = 'false';
            wr.appendChild(hid);
            const chk = document.createElement('input');
            chk.type = 'checkbox';
            chk.id = 'archive';
            chk.name = 'archive';
            chk.value = 'true';
            if (item && item.archive) {
                chk.checked = true;
                archiveState.checked = true;
            }
            chk.addEventListener('change', () => {
                archiveState.checked = chk.checked;
                updateStockControls();
            });
            wr.appendChild(chk);
        } else if (field === 'availableElectroTypeIds') {
            const titleEl = document.createElement('div');
            titleEl.textContent = 'Типы товаров, которые может продавать сотрудник';
            titleEl.style.marginBottom = '4px';
            wr.appendChild(titleEl);
            const box = document.createElement('div');
            box.id = 'types-wrapper';
            box.style.maxHeight = '120px';
            box.style.overflowY = 'auto';
            box.style.border = '1px solid #ccc';
            box.style.padding = '8px';
            wr.appendChild(box);
            const tdData = await fetchJSON(`${cfg.refs[field].endpoint}?page=0&size=1000`);
            const tdList = Array.isArray(tdData.content) ? tdData.content : tdData;
            const exist = item && Array.isArray(item[field]) ? item[field] : [];
            tdList.forEach(it => {
                const row = document.createElement('div');
                row.style.marginBottom = '4px';
                const chk2 = document.createElement('input');
                chk2.type = 'checkbox';
                chk2.name = field;
                chk2.value = it.id;
                chk2.style.marginRight = '6px';
                if (exist.includes(it.id)) chk2.checked = true;
                row.appendChild(chk2);
                row.appendChild(document.createTextNode(it[cfg.refs[field].nameField]));
                box.appendChild(row);
            });
        } else if (field === 'gender') {
            const hid = document.createElement('input');
            hid.type = 'hidden';
            hid.name = 'gender';
            hid.value = 'false';
            wr.appendChild(hid);
            ['мужской', 'женский'].forEach((txt, i) => {
                const lab2 = document.createElement('label');
                const rd = document.createElement('input');
                rd.type = 'radio';
                rd.name = 'gender';
                rd.value = i === 0 ? 'true' : 'false';
                rd.required = true;
                if (item && String(item.gender) === rd.value) rd.checked = true;
                lab2.appendChild(rd);
                lab2.appendChild(document.createTextNode(' ' + txt));
                wr.appendChild(lab2);
            });
        } else if (cfg.refs && cfg.refs[field]) {
            const {endpoint, nameField} = cfg.refs[field];
            const sel2 = document.createElement('select');
            sel2.id = field;
            sel2.name = field;
            sel2.required = true;
            sel2.style.width = '100%';
            wr.appendChild(sel2);
            await populateSelect(sel2, `${endpoint}?page=0&size=1000`, nameField);
            if (item && item[field] != null) sel2.value = item[field];
        } else if (field.toLowerCase().includes('date')) {
            const inp = document.createElement('input');
            if (field === 'birthDate') {
                inp.type = 'date';
                if (item && item[field]) inp.value = parseDate(item[field]);
            } else {
                inp.type = 'datetime-local';
                if (item && item[field]) inp.value = parseDate(item[field]);
            }
            inp.id = field;
            inp.name = field;
            inp.required = true;
            inp.style.width = inp.type === 'date' ? '150px' : '200px';
            wr.appendChild(inp);
        } else {
            const inp = document.createElement('input');
            inp.id = field;
            inp.name = field;
            inp.required = true;
            if (numericFields.includes(field)) {
                inp.type = 'number';
                inp.min = '0';
                inp.step = '1';
                inp.style.width = '100px';
                if (item && item[field] != null) inp.value = item[field];
            } else {
                inp.type = 'text';
                inp.style.width = '100%';
                if (item && item[field] != null) inp.value = item[field];
            }
            wr.appendChild(inp);
        }
        form.appendChild(wr);
    }
    const ac = document.createElement('div');
    ac.style.display = 'flex';
    ac.style.justifyContent = 'flex-start';
    ac.style.gap = '8px';
    ac.style.marginTop = '12px';
    const btnSave2 = document.createElement('button');
    btnSave2.type = 'submit';
    btnSave2.textContent = 'Сохранить';
    btnSave2.className = 'btn';
    const btnCancel2 = document.createElement('button');
    btnCancel2.type = 'button';
    btnCancel2.textContent = 'Отмена';
    btnCancel2.className = 'btn';
    btnCancel2.addEventListener('click', () => (container.innerHTML = ''));
    ac.appendChild(btnSave2);
    ac.appendChild(btnCancel2);
    form.appendChild(ac);
    container.appendChild(form);
    form.addEventListener('submit', async e => {
        e.preventDefault();
        const dto = {};
        const fd2 = new FormData(form);
        for (const field of cfg.createFields) {
            if (field === 'stocks') {
                const arr = [];
                if (!archiveState.checked) {
                    form
                        .querySelectorAll('select[name="stocks.shopId"]')
                        .forEach((s, i) => {
                            const c = form.querySelectorAll('input[name="stocks.count"]')[i].value;
                            arr.push({shopId: Number(s.value), count: Number(c)});
                        });
                }
                dto.stocks = arr;
            } else if (field === 'archive') {
                dto.archive = form.querySelector('input[name="archive"]').checked;
            } else if (field === 'availableElectroTypeIds') {
                dto.availableElectroTypeIds = Array.from(
                    form.querySelectorAll(`input[name="${field}"]:checked`)
                ).map(c => Number(c.value));
            } else if (field === 'gender') {
                dto.gender = form.querySelector('input[name="gender"]:checked').value === 'true';
            } else {
                let v = fd2.get(field);
                if (numericFields.includes(field)) v = Number(v);
                dto[field] = v;
            }
        }
        const url = item ? `${cfg.endpoint}/${item.id}` : cfg.endpoint;
        const method = item ? 'PUT' : 'POST';
        const res = await fetch(url, {
            method,
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(dto),
        });
        if (res.ok) {
            container.innerHTML = '';
            loadData();
        } else {
            const err = await res.json();
            alert(err.message || 'Ошибка');
        }
    });
}

function parseDate(input) {
    const re = /^(\d{2})\.(\d{2})\.(\d{4})(?:\s+(\d{2}):(\d{2}))?$/;
    const m = input.match(re);
    if (!m) return null;
    const [, day, month, year, hour, minute] = m;
    if (hour != null && minute != null) {
        return `${year}-${month}-${day}T${hour}:${minute}`;
    }
    return `${year}-${month}-${day}`;
}

addBtn.addEventListener('click', () => showForm());
initToggles();
initSectionLinks();
initPagination();
initUpload();
loadData();
