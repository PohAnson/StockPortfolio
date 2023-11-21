import datetime as dt

from marshmallow import Schema, fields, post_dump, post_load, validate

from data.stock_code_name_dict import stock_code_name_dict
from server.model.transaction import Transaction


class TransactionSchema(Schema):
    _id = fields.String(required=True)
    date = fields.Date("iso", required=True)
    code = fields.String(
        required=True,
        validate=validate.OneOf(
            stock_code_name_dict.keys(), error="Invalid stock code given."
        ),
    )
    type_ = fields.String(
        required=True,
        validate=validate.OneOf(
            ["buy", "sell"], error="Invalid type_ selected."
        ),
    )
    price = fields.Float(
        required=True,
        validate=validate.Range(min=0, error="Price should not be negative"),
    )
    volume = fields.Integer(
        required=True,
        strict=True,
        validate=validate.Range(min=1),
    )
    broker = fields.String(
        required=True,
        validate=validate.OneOf(["poems", "moomoo"]),
    )
    userid = fields.String(required=True)
    last_modified = fields.DateTime(
        "iso",
        load_default=dt.datetime.utcnow(),
    )

    @post_load
    def make_transaction(self, data, **kwargs):
        return Transaction(**data)

    @post_dump
    def post_dump(self, data, **kwargs):
        return data


class NamedTransactionSchema(TransactionSchema):
    # Includes the name of the stock
    name = fields.String(required=True)
