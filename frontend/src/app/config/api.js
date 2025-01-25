const API_BASE_URL = 'http://localhost:8082';

export const DEFAULT_HEADERS = {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
};

export const DEFAULT_OPTIONS = {
    headers: DEFAULT_HEADERS,
    credentials: 'include'
};

export const API_URLS = {
    // Comercio endpoints
    COMERCIO: {
        BASE: `${API_BASE_URL}/v1/comercios`,
        GET_BY_CODE: (codigo) => `${API_BASE_URL}/v1/comercios/${codigo}`,
        UPDATE_STATUS: (codigo) => `${API_BASE_URL}/v1/comercios/${codigo}/estado`,
        UPDATE_PAGOS: (codigo) => `${API_BASE_URL}/v1/comercios/${codigo}/pagos-aceptados`,
        ASSIGN_COMISION: (codigoComercio, codigoComision) => 
            `${API_BASE_URL}/v1/comercios/${codigoComercio}/comision/${codigoComision}`,
        BY_STATUS: (estado) => `${API_BASE_URL}/v1/comercios/estado/${estado}`,
        SEARCH: `${API_BASE_URL}/v1/comercios/buscar`,
    },
    
    // POS Comercio endpoints
    POS_COMERCIO: {
        BASE: `${API_BASE_URL}/v1/pos-comercio`,
        GET_BY_ID: (codigoPos, tipo) => `${API_BASE_URL}/v1/pos-comercio/${codigoPos}/${tipo}`,
        ACTIVATE: (codigoPos, tipo) => `${API_BASE_URL}/v1/pos-comercio/${codigoPos}/${tipo}/activar`,
        INACTIVATE: (codigoPos, tipo) => `${API_BASE_URL}/v1/pos-comercio/${codigoPos}/${tipo}/inactivar`,
        UPDATE_LAST_USE: (codigoPos, tipo) => `${API_BASE_URL}/v1/pos-comercio/${codigoPos}/${tipo}/ultimo-uso`,
        CHANGE_COMERCIO: (codigoPos, tipo, nuevoCodigoComercio) => 
            `${API_BASE_URL}/v1/pos-comercio/${codigoPos}/${tipo}/comercio/${nuevoCodigoComercio}`,
        GET_CONFIG: (codigoPos, tipo) => `${API_BASE_URL}/v1/pos-comercio/configuracion/${codigoPos}/${tipo}`,
        UPDATE_CONFIG: `${API_BASE_URL}/v1/pos-comercio/configuracion`,
    },

    // Comision endpoints
    COMISION: {
        BASE: `${API_BASE_URL}/v1/comisiones`,
        GET_BY_ID: (codigo) => `${API_BASE_URL}/v1/comisiones/${codigo}`,
    },

    // Comision Segmento endpoints
    COMISION_SEGMENTO: {
        BASE: `${API_BASE_URL}/v1/comision-segmentos`,
        GET_BY_ID: (comision, transaccionesDesde) => 
            `${API_BASE_URL}/v1/comision-segmentos/${comision}/${transaccionesDesde}`,
        UPDATE: (comision, transaccionesDesde) => 
            `${API_BASE_URL}/v1/comision-segmentos/${comision}/${transaccionesDesde}`,
    }
};

// Función helper para hacer peticiones
export const fetchWithConfig = async (url, options = {}) => {
    const finalOptions = {
        ...DEFAULT_OPTIONS,
        ...options,
        headers: {
            ...DEFAULT_HEADERS,
            ...options.headers
        }
    };

    const response = await fetch(url, finalOptions);
    if (!response.ok) {
        throw new Error(`Error en la petición: ${response.statusText}`);
    }
    return response;
};

export default API_URLS; 