-- 1. Devuelve un listado de todos los cursos que se realizaron con fecha de inicio y fin durante el año 2025,
--  cuyo precio base sea superior a 500€.


-- 2. Devuelve un listado de todos los alumnos que NO se han matriculado en ningún curso.

SELECT *
FROM matricula matr
INNER JOIN alumno alum
ON alum.alumno_id = matr.alumno_id
WHERE matr.;

-- 3. Devuelve una lista de los id's, nombres y emails de los alumnos que no tienen el teléfono registrado.
use academia;
SELECT * FROM  alumno alum WHERE alum.telefono = null;

-- 4. Devuelva un listado con los id's y emails de los alumnos que se hayan registrado con una cuenta de yahoo.es.


-- 5. Devuelva un listado de los alumnos cuyo primer apellido es Martín. El listado tiene que estar ordenado
-- por fecha de alta en la academia de más reciente a menos reciente y nombre y apellidos en orden alfabético.


-- 6. Devuelva gasto total (pagado) que ha realizado la alumna Claudia López Rodríguez en cursos en la academia.


-- 7. Devuelva el curso al que se le ha aplicado el mayor descuento en cuantía para una matrícula
-- (no tiene por qué ser de descuento porcentual) sobre su precio base.



-- 8. Devuelva el curso al que se le ha aplicado el mayor descuento en cuantía para una matrícula
-- (no tiene por qué ser correspondiente al mayor descuento porcentual) sobre su precio base.



-- 9. Devuelva los alumnos que hayan obtenido un 10 como nota final en algún curso del que se han matriculado.



-- 10. Devuelva el valor de la mínima nota obtenida en un curso.



