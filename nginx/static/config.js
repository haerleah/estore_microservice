const sectionConfig = {
    electro: {
        endpoint: '/estore/api/item',
        fields: ['id', 'name', 'type', 'price', 'count', 'archive', 'description'],
        labels: {
            id: 'ID',
            name: 'Наименование',
            type: 'Тип',
            price: 'Цена',
            count: 'Количество',
            archive: 'Архивное',
            description: 'Описание'
        },
        createFields: ['name', 'electroTypeId', 'archive', 'price', 'description', 'stocks'],
        refs: {
            electroTypeId: {endpoint: 'estore/api/item-type', nameField: 'name'},
            shopId: {endpoint: 'estore/api/shop', nameField: 'name'}
        }
    },
    purchases: {
        endpoint: '/estore/api/purchase',
        fields: ['id', 'electroItemName', 'employeeName', 'purchaseDate', 'shopName', 'purchaseType'],
        labels: {
            id: 'ID',
            electroItemName: 'Наименование товара',
            employeeName: 'ФИО сотрудника',
            purchaseDate: 'Дата покупки',
            shopName: 'Название магазина',
            purchaseType: 'Способ оплаты'
        },
        createFields: ['electroItemId', 'employeeId', 'purchaseDate', 'shopId', 'purchaseTypeId'],
        refs: {
            electroItemId: {endpoint: '/estore/api/item', nameField: 'name'},
            employeeId: {
                endpoint: '/estore/api/employee',
                nameField: item => `${item.lastName} ${item.firstName} ${item.patronymic}`
            },
            shopId: {endpoint: '/estore/api/shop', nameField: 'name'},
            purchaseTypeId: {endpoint: '/estore/api/purchase-type', nameField: 'name'}
        }
    },
    employees: {
        endpoint: '/estore/api/employee',
        fields: ['id', 'lastName', 'firstName', 'patronymic', 'birthDate', 'position', 'shopName', 'gender'],
        labels: {
            id: 'ID',
            lastName: 'Фамилия',
            firstName: 'Имя',
            patronymic: 'Отчество',
            birthDate: 'Дата рождения',
            position: 'Должность',
            shopName: 'Название магазина',
            gender: 'Пол'
        },
        createFields: ['firstName', 'lastName', 'patronymic', 'birthDate',
            'positionId', 'shopId', 'gender', 'availableElectroTypeIds'],
        refs: {
            shopId: {endpoint: '/estore/api/shop', nameField: 'name'},
            positionId: {endpoint: '/estore/api/position', nameField: 'name'},
            availableElectroTypeIds: {
                endpoint: '/estore/api/item-type',
                nameField: 'name'
            },
        }
    },
    positions: {
        endpoint: '/estore/api/position',
        fields: ['id', 'name'],
        labels: {
            id: 'ID',
            name: 'Название'
        },
        createFields: ['name']
    },
    electronics: {
        endpoint: '/estore/api/item-type',
        fields: ['id', 'name'],
        labels: {
            id: 'ID',
            name: 'Название'
        },
        createFields: ['name']
    },
    store: {
        endpoint: '/estore/api/shop',
        fields: ['id', 'name', 'address'],
        labels: {
            id: 'ID',
            name: 'Название',
            address: 'Адрес магазина'
        },
        createFields: ['name', 'address']
    },
    purchaseType: {
        endpoint: '/estore/api/purchase-type',
        fields: ['id', 'name'],
        labels: {
            id: 'ID',
            name: 'Название'
        },
        createFields: ['name']
    }
};

export default sectionConfig;