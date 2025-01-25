"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { FaEdit, FaTrash, FaPlus, FaEye, FaSearch } from "react-icons/fa";
import { API_URLS, fetchWithConfig } from "../../config/api";
import "../../Css/general.css";

const CrudTable = () => {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [filterEstado, setFilterEstado] = useState("");
  const router = useRouter();

  useEffect(() => {
    fetchComercios();
  }, []);

  const fetchComercios = async () => {
    try {
      let url = API_URLS.COMERCIO.BASE;
      if (filterEstado) {
        url = API_URLS.COMERCIO.BY_STATUS(filterEstado);
      }
      const response = await fetchWithConfig(url);
      const result = await response.json();
      setData(result);
      setError(null);
    } catch (err) {
      setError('Error al cargar los datos: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = async () => {
    try {
      setLoading(true);
      const response = await fetchWithConfig(`${API_URLS.COMERCIO.SEARCH}?criterio=${searchTerm}`);
      const result = await response.json();
      setData(result);
      setError(null);
    } catch (err) {
      setError('Error en la búsqueda: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    router.push('/GtwComercio/create');
  };

  const handleUpdate = async (codigo) => {
    router.push(`/GtwComercio/update/${codigo}`);
  };

  const handleView = async (codigo) => {
    router.push(`/GtwComercio/read/${codigo}`);
  };

  const handleUpdateStatus = async (codigo, nuevoEstado) => {
    try {
      await fetchWithConfig(API_URLS.COMERCIO.UPDATE_STATUS(codigo), {
        method: 'PUT',
        body: JSON.stringify({ nuevoEstado })
      });
      await fetchComercios();
    } catch (err) {
      setError('Error al actualizar el estado: ' + err.message);
    }
  };

  if (loading) return <div className="loading">Cargando...</div>;
  if (error) return <div className="error">{error}</div>;

  return (
    <div className="main-container">
      <div className="table-header">
        <div className="header-content">
          <h1 className="table-title">Comercios Registrados</h1>
          <div className="search-container">
            <input
              type="text"
              placeholder="Buscar por razón social o nombre comercial..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="search-input"
            />
            <button onClick={handleSearch} className="search-button">
              <FaSearch /> Buscar
            </button>
          </div>
          <select
            value={filterEstado}
            onChange={(e) => {
              setFilterEstado(e.target.value);
              fetchComercios();
            }}
            className="filter-select"
          >
            <option value="">Todos los estados</option>
            <option value="ACT">Activo</option>
            <option value="INA">Inactivo</option>
            <option value="SUS">Suspendido</option>
          </select>
        </div>
        <div className="table-summary">
          <div className="summary-item">
            <span className="summary-label">Total Comercios:</span>
            <span className="summary-value">{data.length}</span>
          </div>
          <div className="summary-item">
            <span className="summary-label">Comercios Activos:</span>
            <span className="summary-value">
              {data.filter(item => item.estado === "ACT").length}
            </span>
          </div>
        </div>
      </div>

      <div className="table-responsive">
        <button className="create-button" onClick={handleCreate}>
          <FaPlus className="button-icon" />
          <span>Nuevo Comercio</span>
        </button>
        <table className="modern-table">
          <thead>
            <tr>
              <th>Código</th>
              <th>Código Interno</th>
              <th>RUC</th>
              <th>Razón Social</th>
              <th>Nombre Comercial</th>
              <th>Código Comisión</th>
              <th>Pagos Aceptados</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {data.map((item, index) => (
              <tr
                key={item.codigo}
                className={`table-row ${index % 2 === 0 ? 'row-even' : 'row-odd'}`}
              >
                <td className="code-cell">{item.codigo}</td>
                <td className="code-cell">{item.codigoInterno}</td>
                <td className="code-cell">{item.ruc}</td>
                <td>{item.razonSocial}</td>
                <td>{item.nombreComercial}</td>
                <td className="code-cell">{item.codigoComision}</td>
                <td className="amount-cell">{item.pagosAceptados}</td>
                <td className="status-cell">
                  <span className={`status-badge ${item.estado.toLowerCase()}`}>
                    {item.estado === 'ACT' ? 'Activo' : 
                     item.estado === 'INA' ? 'Inactivo' : 'Suspendido'}
                  </span>
                </td>
                <td className="actions-cell">
                  <button
                    onClick={() => handleView(item.codigo)}
                    className="action-button view"
                    title="Ver detalles"
                  >
                    <FaEye />
                  </button>
                  <button
                    onClick={() => handleUpdate(item.codigo)}
                    className="action-button edit"
                    title="Editar"
                  >
                    <FaEdit />
                  </button>
                  {item.estado === 'ACT' ? (
                    <button
                      onClick={() => handleUpdateStatus(item.codigo, 'INA')}
                      className="action-button suspend"
                      title="Inactivar"
                    >
                      <FaTrash />
                    </button>
                  ) : (
                    <button
                      onClick={() => handleUpdateStatus(item.codigo, 'ACT')}
                      className="action-button activate"
                      title="Activar"
                    >
                      <FaPlus />
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CrudTable;
