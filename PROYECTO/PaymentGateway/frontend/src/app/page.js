"use client";

import { useRouter } from "next/navigation";

const MainPage = () => {
  const router = useRouter();

  const tables = [
    { id: 1, name: "GtwFacturacionComercio", route: "/GtwFacturacionComercio/components" },
    { id: 2, name: "GtwSeguridadMarca", route: "/GtwSeguridadMarca/components" },
    { id: 3, name: "GtwPosComercio", route: "/GtwPosComercio/components" },
    { id: 4, name: "GtwComercio", route: "/GtwComercio/components" },
    { id: 5, name: "GtwSeguridadProcesador", route: "/GtwSeguridadProcesador/components" },
    { id: 6, name: "GtwComisionSegmento", route: "/GtwComisionSegmento/components" },
    { id: 7, name: "GtwTransaccion", route: "/GtwTransaccion/components" },
    { id: 8, name: "GtwComision", route: "/GtwComision/components" },
    { id: 9, name: "GtwSeguridad", route: "/GtwSeguridad/components" },
  ];

  const handleNavigate = (route) => {
    router.push(route);
  };

  return (
    <div
      style={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "100vh",
        backgroundColor: "#1e293b", // Fondo oscuro
        padding: "1rem",
      }}
    >
      <div
        style={{
          width: "100%",
          maxWidth: "900px",
          backgroundColor: "#ffffff", // Fondo blanco para el panel
          borderRadius: "16px",
          boxShadow: "0 12px 24px rgba(0, 0, 0, 0.2)",
          padding: "2rem",
          fontFamily: "'Inter', sans-serif",
        }}
      >
        <header
          style={{
            borderBottom: "2px solid #e5e7eb",
            marginBottom: "1.5rem",
            paddingBottom: "1rem",
          }}
        >
          <h1
            style={{
              fontSize: "2rem",
              fontWeight: "700",
              color: "#1e3a8a", // Azul original
              textAlign: "center",
              marginBottom: "0.5rem",
            }}
          >
            Gestión de Tablas
          </h1>
          <p
            style={{
              textAlign: "center",
              color: "#6b7280", // Gris original
              fontSize: "1.1rem",
            }}
          >
            Selecciona una tabla para gestionar los datos correspondientes.
          </p>
        </header>
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))",
            gap: "1rem",
          }}
        >
          {tables.map((table) => (
            <div
              key={table.id}
              style={{
                backgroundColor: "#3b82f6", // Azul original para las tarjetas
                borderRadius: "12px",
                boxShadow: "0 6px 12px rgba(0, 0, 0, 0.3)",
                padding: "1.5rem",
                textAlign: "center",
                transition: "transform 0.3s, box-shadow 0.3s",
                cursor: "pointer",
              }}
              onClick={() => handleNavigate(table.route)}
              onMouseEnter={(e) => {
                e.currentTarget.style.boxShadow = "0 10px 20px rgba(0, 0, 0, 0.4)";
                e.currentTarget.style.transform = "translateY(-4px)";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.boxShadow = "0 6px 12px rgba(0, 0, 0, 0.3)";
                e.currentTarget.style.transform = "translateY(0)";
              }}
            >
              <h2
                style={{
                  fontSize: "1.2rem",
                  fontWeight: "600",
                  color: "#ffffff", // Blanco para texto
                  marginBottom: "0.5rem",
                }}
              >
                {table.name}
              </h2>
              <p
                style={{
                  fontSize: "0.95rem",
                  color: "#dbeafe", // Azul claro para subtítulos
                }}
              >
                Hacer clic para gestionar
              </p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default MainPage;
