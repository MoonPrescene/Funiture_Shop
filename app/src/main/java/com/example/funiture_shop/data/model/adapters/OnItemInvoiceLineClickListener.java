package com.example.funiture_shop.data.model.adapters;

import com.example.funiture_shop.data.model.entity.InvoiceLine;

public interface OnItemInvoiceLineClickListener {
    void selectInvoiceLine(InvoiceLine invoiceLine, int index);
    void add(InvoiceLine invoiceLine, int index);
    void sub(InvoiceLine invoiceLine, int index);
}
