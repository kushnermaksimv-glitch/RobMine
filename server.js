const server = require('http').createServer();
const io = require('socket.io')(server, {
    cors: { origin: "*" } // Разрешаем подключения из мобильных WebView приложений
});

const players = {};

io.on('connection', (socket) => {
    console.log('Игрок вошел в сеть:', socket.id);

    // Получение стартовых данных от вошедшего игрока
    socket.on('init', (data) => {
        players[socket.id] = {
            id: socket.id,
            x: data.x || 0,
            y: data.y || 1.8,
            z: data.z || -5,
            type: data.type || 'minecraft'
        };
        // Отправляем игроку список тех, кто уже на сервере
        socket.emit('currentPlayers', players);
        // Оповещаем остальных игроков о новом пользователе
        socket.broadcast.emit('newPlayer', players[socket.id]);
    });

    // Прием и трансляция координат движения
    socket.on('move', (data) => {
        if (players[socket.id]) {
            players[socket.id].x = data.x;
            players[socket.id].y = data.y;
            players[socket.id].z = data.z;
            // Мгновенная отправка позиции всем остальным клиентам
            socket.broadcast.emit('playerMoved', players[socket.id]);
        }
    });

    // Обработка отключения от сети
    socket.on('disconnect', () => {
        console.log('Игрок покинул сеть:', socket.id);
        delete players[socket.id];
        io.emit('playerDisconnected', socket.id);
    });
});

// Запуск на порту облачного провайдера или локальном 3000
const PORT = process.env.PORT || 3000;
server.listen(PORT, () => {
    console.log(`Мультиплеерный сервер запущен на порту ${PORT}`);
});
