use pollosHermanos;
ALTER TABLE menu
ADD stock INT DEFAULT 0;

SHOW TABLES;
DESCRIBE pedidos;
DESCRIBE mesas;
DESCRIBE detalle_pedido;


CREATE TABLE menu_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT,
    id_producto_inv INT,
    cantidad_usada DECIMAL(10,2),
    FOREIGN KEY (id_producto) REFERENCES menu(id_producto),
    FOREIGN KEY (id_producto_inv) REFERENCES inventario(id_producto_inv)
);

INSERT INTO menu_inventario (id_producto, id_producto_inv, cantidad_usada) VALUES
-- Papas Hermanas (24) → Papas, Aceite
(24, 3, 0.20),
(24, 2, 0.10),

-- Nuggets Azules (25) → Pollo, Harina, Aceite
(25, 1, 0.25),
(25, 5, 0.10),
(25, 2, 0.10),

-- Combo Los Pollos Hermanos (26) → Pollo, Papas, Refresco, Aceite
(26, 1, 0.40),
(26, 3, 0.20),
(26, 4, 1.00),
(26, 2, 0.15),

-- Pollo Extra Crispy Salamanca (27) → Pollo, Harina, Aceite, Salsa Especial
(27, 1, 0.50),
(27, 5, 0.15),
(27, 2, 0.20),
(27, 6, 0.10),

-- Hamburguesa Gustavo (28) → Pollo, Harina, Aceite, Salsa Especial
(28, 1, 0.35),
(28, 5, 0.10),
(28, 2, 0.10),
(28, 6, 0.05),

-- Tacos Fring (29) → Pollo, Salsa Especial, Harina
(29, 1, 0.30),
(29, 6, 0.10),
(29, 5, 0.10),

-- Mega Banquete Cartel (30) → Pollo, Papas, Aceite, Salsa Especial
(30, 1, 1.50),
(30, 3, 0.50),
(30, 2, 0.40),
(30, 6, 0.20),

-- Pay de Limón Jesse (31) → Harina
(31, 5, 0.20),

-- Helado Crystal Blue (32) → sin ingredientes del inventario, se omite

-- Refresco Schrader (33) → Refresco
(33, 4, 1.00),

-- Malteada Azul (34) → Refresco
(34, 4, 1.00),

-- Café DEA (35) → sin ingredientes del inventario, se omite

-- Pizza Hawaiana (37) → Harina, Salsa Especial, Aceite
(37, 5, 0.30),
(37, 6, 0.15),
(37, 2, 0.10);

ALTER TABLE pedidos 
MODIFY COLUMN estado ENUM('Pendiente','Preparando','Listo','Entregado','Cancelado','Pagado') 
DEFAULT 'Pendiente';





SELECT id_reservacion,
       nombre_cliente,
       codigo_reserva
FROM reservaciones;





-- nuevo
CREATE TABLE folios(

id_folio INT AUTO_INCREMENT
PRIMARY KEY,

numero_folio VARCHAR(50)
UNIQUE NOT NULL,

id_pedido INT
NOT NULL,

numero_mesa INT
NOT NULL,

metodo_pago VARCHAR(50)
NOT NULL,

total DOUBLE
NOT NULL,

fecha_generacion TIMESTAMP
DEFAULT CURRENT_TIMESTAMP,

FOREIGN KEY(id_pedido)
REFERENCES pedidos(id_pedido)

);




SELECT 
p.id_pedido,
m.numero_mesa,
dp.id_detalle,
dp.subtotal
FROM pedidos p
LEFT JOIN detalle_pedido dp
ON p.id_pedido=dp.id_pedido
LEFT JOIN mesas m
ON p.id_mesa=m.id_mesa;

DELETE FROM pagos;
DELETE FROM detalle_pedido;
DELETE FROM pedidos;

ALTER TABLE pagos AUTO_INCREMENT=1;
ALTER TABLE detalle_pedido AUTO_INCREMENT=1;
ALTER TABLE pedidos AUTO_INCREMENT=1;

DESCRIBE pagos;

ALTER TABLE pagos
MODIFY metodo_pago
ENUM('Efectivo','Tarjeta','Transferencia');

DELETE FROM pagos;

DELETE FROM pagos
WHERE id_pago > 0;

ALTER TABLE pagos
MODIFY metodo_pago
ENUM('Efectivo','Tarjeta','Transferencia');

SELECT * FROM detalle_pedido;

SELECT id_pedido, subtotal
FROM detalle_pedido;

SELECT
p.id_pedido,
SUM(dp.subtotal) AS total
FROM pedidos p
LEFT JOIN detalle_pedido dp
ON p.id_pedido=dp.id_pedido
GROUP BY p.id_pedido;

SELECT * 
FROM detalle_pedido
WHERE id_pedido IN (6,7,8,9,10);

SELECT p.id_pedido,
       dp.id_detalle,
       dp.subtotal
FROM pedidos p
LEFT JOIN detalle_pedido dp
ON p.id_pedido = dp.id_pedido
ORDER BY p.id_pedido;

ALTER TABLE pagos
MODIFY metodo_pago VARCHAR(50);

-- PRODUCTOS TEMATICA LOS POLLOS HERMANOS
INSERT INTO menu 
(nombre, descripcion, precio, id_categoria, stock) 
VALUES

-- Entradas
('Aros de Cebolla Heisenberg', 
'Crujientes aros dorados con salsa secreta.', 
75.00, 1, 20),

('Papas Hermanas', 
'Papas fritas sazonadas estilo Pollos Hermanos.', 
65.00, 1, 25),

('Nuggets Azules', 
'Trozos de pollo empanizado edición especial.', 
89.00, 1, 18),

-- Platillos
('Combo Los Pollos Hermanos', 
'2 piezas de pollo, papas y refresco.', 
159.00, 2, 15),

('Pollo Extra Crispy Salamanca', 
'Pollo picante para valientes.', 
185.00, 2, 10),

('Hamburguesa Gustavo', 
'Hamburguesa premium con pollo crispy.', 
145.00, 2, 14),

('Tacos Fring', 
'3 tacos de pollo con salsa especial.', 
130.00, 2, 22),

('Mega Banquete Cartel', 
'8 piezas de pollo para compartir.', 
399.00, 2, 5),

-- Postres
('Pay de Limón Jesse', 
'Pay frío con toque cítrico.', 
70.00, 3, 12),

('Helado Crystal Blue', 
'Helado azul de vainilla.', 
80.00, 3, 9),

-- Bebidas
('Refresco Schrader', 
'Refresco grande.', 
45.00, 4, 40),

('Malteada Azul', 
'Malteada cremosa edición especial.', 
65.00, 4, 13),

('Café DEA', 
'Café americano fuerte.', 
40.00, 4, 30);

DELETE FROM detalle_pedido;
DELETE FROM pedidos;
DELETE FROM menu
WHERE stock = 0;
select * from menu;