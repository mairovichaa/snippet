package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.factory_for_generic;

class Option2 {

    interface Converter {

        void convert(Entity e);

    }

    interface TypedConverter<E extends Entity> {

        void typedConvert(E e);

    }

    static class ConverterImpl implements Converter, TypedConverter<EntityImpl> {

        @Override
        public void convert(Entity e) {
            if (e instanceof EntityImpl) {
                EntityImpl ei = (EntityImpl) e;
                typedConvert(ei);
            }
        }


        @Override
        public void typedConvert(EntityImpl entity) {

        }

    }

    static class ConverterImpl2 implements Converter, TypedConverter<EntityImpl2> {

        @Override
        public void convert(Entity e) {
            if (e instanceof EntityImpl) {
                EntityImpl2 ei = (EntityImpl2) e;
                typedConvert(ei);
            }
        }

        @Override
        public void typedConvert(EntityImpl2 entityImpl2) {

        }

    }

    static class Factory {

        private final Converter converterImpl = new ConverterImpl();
        private final Converter converterImpl2 = new ConverterImpl2();

        Converter getConverter1(int type) {
            return type == 1 ? converterImpl : converterImpl2;
        }

    }

    static TypedConverter<? super Entity> converter() {
        return (TypedConverter<Entity>) entity -> {

        };
    }

    public static void main(String[] args) {
        Factory factory = new Factory();
        EntityImpl entityImplObj = new EntityImpl();
        EntityImpl2 entityImpl2Obj = new EntityImpl2();

        Converter converter = factory.getConverter1(1);
        converter.convert(entityImplObj);
        converter.convert(entityImpl2Obj);

//       need this type of converter to do generic conversion without warnings
//       the problem is that both upper and lower bounds are Entity, so there is only one
//       type, which could be used TypedConverter<Entity>
        TypedConverter<? super Entity> converter1 = converter();
        converter1.typedConvert(entityImplObj);
        converter1.typedConvert(entityImpl2Obj);
    }


}
