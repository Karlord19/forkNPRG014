class DynamicMoney {
    Integer amount
    String currency

    DynamicMoney plus(DynamicMoney other) {
        if (this.currency != other.currency) throw new IllegalArgumentException('Cannot add different currencies');
        new DynamicMoney(amount: this.amount + other.amount, currency: this.currency)
    }

    public String toString() {
        "${amount} ${currency}"
    }
}

//TASK Enhance the Integer class with currencies so that the following code passes
Integer.metaClass.getEur = { ->
    new DynamicMoney(amount: delegate, currency: 'eur')
}
Integer.metaClass.getUsd = { ->
    new DynamicMoney(amount: delegate, currency: 'usd')
}

// u cant have Usd and usd at the same time
// it will just take the getUsd thing and accept Usd and usd

println 10.eur
println 10.eur + 20.eur
println 10.usd + 20.usd
