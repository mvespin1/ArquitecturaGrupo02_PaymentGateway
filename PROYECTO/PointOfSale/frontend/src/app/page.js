"use client";

import { useState } from "react";
import "./index.css";

const MainPage = () => {
  const [formData, setFormData] = useState({
    cardNumber: "",
    expiryDate: "",
    cvv: "",
    cardName: "MasterCard",
    transactionAmount: "",
  });

  const [errors, setErrors] = useState({}); // Estado para los errores de validación

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    // Validación para el CVV (máximo 3 dígitos)
    if (name === "cvv" && value.length > 3) return;

    // Formateo del número de tarjeta (cada 4 dígitos con espacio)
    if (name === "cardNumber") {
      const formattedValue = value
        .replace(/\D/g, "") // Eliminar caracteres no numéricos
        .replace(/(\d{4})(?=\d)/g, "$1 "); // Agregar un espacio cada 4 dígitos
      setFormData({ ...formData, [name]: formattedValue });
      return;
    }

    setFormData({
      ...formData,
      [name]: value,
    });

    // Limpiar el error del campo actual al escribir
    setErrors({
      ...errors,
      [name]: "",
    });
  };

  const handleFormSubmit = (e) => {
    e.preventDefault();

    // Validaciones
    const newErrors = {};

    // Validación del número de tarjeta
    const cleanCardNumber = formData.cardNumber.replace(/\s/g, ""); // Eliminar espacios para validación
    if (!cleanCardNumber || !/^\d{16}$/.test(cleanCardNumber)) {
      newErrors.cardNumber = "El número de la tarjeta debe tener 16 dígitos.";
    }

    // Validación de fecha de vencimiento
    if (!formData.expiryDate || !/^(0[1-9]|1[0-2])\/\d{2}$/.test(formData.expiryDate)) {
      newErrors.expiryDate = "La fecha de vencimiento debe estar en formato MM/YY.";
    } else {
      const [month, year] = formData.expiryDate.split("/").map(Number);
      const currentYear = new Date().getFullYear() % 100; // Últimos dos dígitos del año actual
      const currentMonth = new Date().getMonth() + 1; // Mes actual

      if (year < 25 || (year === currentYear && month < currentMonth)) {
        newErrors.expiryDate = "La fecha de vencimiento debe ser a partir del año 2025.";
      }
    }

    // Validación de CVV
    if (!formData.cvv || formData.cvv.length !== 3) {
      newErrors.cvv = "El CVV debe tener exactamente 3 dígitos.";
    }

    // Validación de monto
    if (!formData.transactionAmount || isNaN(formData.transactionAmount)) {
      newErrors.transactionAmount = "El monto debe ser un número válido.";
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    const isSuccessful = Math.random() > 0.5;

    if (isSuccessful) {
      alert("Transacción realizada con éxito.");
    } else {
      alert("Transacción rechazada. Inténtalo nuevamente.");
    }
  };

  return (
    <main className="main-container">
      <h1 className="main-title">Realizar Transacción</h1>
      <form onSubmit={handleFormSubmit} className="form">
        <div className="form-group">
          <label htmlFor="cardNumber">Número de la Tarjeta</label>
          <input
            type="text"
            id="cardNumber"
            name="cardNumber"
            placeholder="1234 5678 9012 3456"
            value={formData.cardNumber}
            onChange={handleInputChange}
            className="form-input"
          />
          {errors.cardNumber && <p className="error-message">{errors.cardNumber}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="expiryDate">Fecha de Vencimiento</label>
          <input
            type="text"
            id="expiryDate"
            name="expiryDate"
            placeholder="MM/YY"
            value={formData.expiryDate}
            onChange={handleInputChange}
            className="form-input"
          />
          {errors.expiryDate && <p className="error-message">{errors.expiryDate}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="cvv">CVV</label>
          <input
            type="text"
            id="cvv"
            name="cvv"
            placeholder="123"
            value={formData.cvv}
            onChange={handleInputChange}
            className="form-input"
          />
          {errors.cvv && <p className="error-message">{errors.cvv}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="cardName">Tipo de Tarjeta</label>
          <select
            id="cardName"
            name="cardName"
            value={formData.cardName}
            onChange={handleInputChange}
            className="form-input"
          >
            <option value="MasterCard">MasterCard</option>
            <option value="Visa">Visa</option>
            <option value="American Express">American Express</option>
          </select>
        </div>
        <div className="form-group">
          <label htmlFor="transactionAmount">Monto de la Transacción</label>
          <input
            type="text"
            id="transactionAmount"
            name="transactionAmount"
            placeholder="100.50"
            value={formData.transactionAmount}
            onChange={handleInputChange}
            className="form-input"
          />
          {errors.transactionAmount && <p className="error-message">{errors.transactionAmount}</p>}
        </div>
        <button type="submit" className="form-button">
          Realizar Transacción
        </button>
      </form>
    </main>
  );
};

export default MainPage;
