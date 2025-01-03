package ec.edu.espe.pos.dto;

import java.math.BigDecimal;

public class SecureCardDataDTO {
    private String encryptedData; // Will contain all sensitive data encrypted
    private String marca;
    private BigDecimal monto;

    public SecureCardDataDTO() {
    }

    public SecureCardDataDTO(String encryptedData, String marca, BigDecimal monto) {
        this.encryptedData = encryptedData;
        this.marca = marca;
        this.monto = monto;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((encryptedData == null) ? 0 : encryptedData.hashCode());
        result = prime * result + ((marca == null) ? 0 : marca.hashCode());
        result = prime * result + ((monto == null) ? 0 : monto.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecureCardDataDTO other = (SecureCardDataDTO) obj;
        if (encryptedData == null) {
            if (other.encryptedData != null)
                return false;
        } else if (!encryptedData.equals(other.encryptedData))
            return false;
        if (marca == null) {
            if (other.marca != null)
                return false;
        } else if (!marca.equals(other.marca))
            return false;
        if (monto == null) {
            if (other.monto != null)
                return false;
        } else if (!monto.equals(other.monto))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SecureCardDataDTO [encryptedData=" + encryptedData + ", marca=" + marca + ", monto=" + monto + "]";
    }
}