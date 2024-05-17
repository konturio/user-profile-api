from sqlalchemy import create_engine, Column, Integer, String, LargeBinary, ForeignKey, UniqueConstraint, DateTime
from sqlalchemy.orm import declarative_base, relationship
from datetime import datetime

Base = declarative_base()


class App(Base):
    __tablename__ = 'app'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String, nullable=False)


class Feature(Base):
    __tablename__ = 'feature'
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String, nullable=False)


class Asset(Base):
    __tablename__ = 'assets'
    __table_args__ = (UniqueConstraint('app_id', 'filename', 'language', name='unique_asset'),)

    id = Column(Integer, primary_key=True, autoincrement=True)
    media_type = Column(String, nullable=False)
    media_subtype = Column(String, nullable=False)
    filename = Column(String, nullable=False)
    description = Column(String)
    owner_user_id = Column(Integer)
    language = Column(String)
    last_updated = Column(DateTime, nullable=False, default=datetime.utcnow)
    app_id = Column(Integer, ForeignKey('app.id'))
    feature_id = Column(Integer, ForeignKey('feature.id'))
    asset = Column(LargeBinary, nullable=False)

    app = relationship("App")
    feature = relationship("Feature")