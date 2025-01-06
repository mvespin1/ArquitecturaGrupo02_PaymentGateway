"use client";

import { useState } from "react";
import "./index.css";

const MainPage = () => {
  const [formData, setFormData] = useState({
    cardNumber: "",
    expiryDate: "",
    cvv: "",
    cardName: "MSCD",
    transactionAmount: "",
    diferido: "No",
    mesesDiferido: "", // Campo para guardar la cantidad de meses
  });

  const [errors, setErrors] = useState({});

  const handleInputChange = (e) => {
    const { name, value } = e.target;

    if (name === "cvv" && value.length > 3) return;

    if (name === "cardNumber") {
      const formattedValue = value
        .replace(/\D/g, "")
        .replace(/(\d{4})(?=\d)/g, "$1 ");
      setFormData({ ...formData, [name]: formattedValue });
      return;
    }

    setFormData({
      ...formData,
      [name]: value,
    });

    setErrors({
      ...errors,
      [name]: "",
    });
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    const newErrors = {};
    const cleanCardNumber = formData.cardNumber.replace(/\s/g, "");
    if (!cleanCardNumber || !/^\d{16}$/.test(cleanCardNumber)) {
      newErrors.cardNumber = "El número de la tarjeta debe tener 16 dígitos.";
    }
    if (
      !formData.expiryDate ||
      !/^(0[1-9]|1[0-2])\/\d{2}$/.test(formData.expiryDate)
    ) {
      newErrors.expiryDate = "La fecha de vencimiento debe estar en formato MM/YY.";
    }
    if (!formData.cvv || formData.cvv.length !== 3) {
      newErrors.cvv = "El CVV debe tener exactamente 3 dígitos.";
    }
    if (!formData.transactionAmount || isNaN(formData.transactionAmount)) {
      newErrors.transactionAmount = "El monto debe ser un número válido.";
    }
    if (formData.diferido === "Sí" && !formData.mesesDiferido) {
      newErrors.mesesDiferido = "Debe seleccionar los meses de diferido.";
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    const transactionPayload = {
      cardNumber: formData.cardNumber.replace(/\s/g, ""),
      expiryDate: formData.expiryDate,
      cvv: formData.cvv,
      cardName: formData.cardName,
      transactionAmount: parseFloat(formData.transactionAmount),
      diferido: formData.diferido,
      mesesDiferido: formData.mesesDiferido,
    };

    console.log("Payload a enviar:", transactionPayload);
    alert("Transacción enviada exitosamente");
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
            <option value="MSCD">MasterCard</option>
            <option value="VISA">Visa</option>
            <option value="AMEX">American Express</option>
            <option value="DINE">Diners Club</option>
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
        <div className="form-group">
          <label>Diferido</label>
          <div style={{ display: "flex", gap: "10px" }}>
            <label>
              <input
                type="radio"
                name="diferido"
                value="Sí"
                checked={formData.diferido === "Sí"}
                onChange={handleInputChange}
              />
              Sí
            </label>
            <label>
              <input
                type="radio"
                name="diferido"
                value="No"
                checked={formData.diferido === "No"}
                onChange={handleInputChange}
              />
              No
            </label>
          </div>
        </div>
        {formData.diferido === "Sí" && (
          <div className="form-group">
            <label htmlFor="mesesDiferido">Meses de Diferido</label>
            <select
              id="mesesDiferido"
              name="mesesDiferido"
              value={formData.mesesDiferido}
              onChange={handleInputChange}
              className="form-input"
            >
              <option value="">Seleccione</option>
              <option value="3">3 meses</option>
              <option value="6">6 meses</option>
              <option value="9">9 meses</option>
              <option value="12">12 meses</option>
            </select>
            {errors.mesesDiferido && (
              <p className="error-message">{errors.mesesDiferido}</p>
            )}
          </div>
        )}
        <button type="submit" className="form-button">
          Realizar Transacción
        </button>
      </form>
    </main>
  );
};

export default MainPage;
