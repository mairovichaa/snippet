package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.factory_for_generic;

class Option1 {

    interface Converter<E extends Entity> {

        void convert(E e);

    }

    static class ConverterImpl implements Converter<EntityImpl> {

        @Override
        public void convert(EntityImpl entity) {
        }

    }

    static class ConverterImpl2 implements Converter<EntityImpl2> {

        @Override
        public void convert(EntityImpl2 entity) {
        }

    }

    static class Factory {

        private final Converter<EntityImpl> converterImpl = new ConverterImpl();
        private final Converter<EntityImpl2> converterImpl2 = new ConverterImpl2();

        Converter getConverter1(int type) {
            return type == 1 ? converterImpl : converterImpl2;
        }

        // Converter<EntityImpl> is not Converter<Entity>
        // Converter<EntityImpl2> is not Converter<Entity>
//        Converter<Entity> getConverter2(int type) {
//            return type == 1 ? converterImpl : converterImpl2;
//        }

        Converter<?> getConverter3(int type) {
            return type == 1 ? converterImpl : converterImpl2;
        }

        Converter<? extends Entity> getConverter4(int type) {
            return type == 1 ? converterImpl : converterImpl2;
        }

        // Converter<EntityImpl> is not Converter<Entity>
        // Converter<EntityImpl2> is not Converter<Entity>
//        Converter<Entity> getConverter5(int type) {
//            return type == 1 ? converterImpl : converterImpl2;
//        }
    }

    public static void main(String[] args) {
        Factory factory = new Factory();
        EntityImpl entityImplObj = new EntityImpl();
        EntityImpl2 entityImpl2Obj = new EntityImpl2();

        // use @SuppressWarnings("unchecked") on the method within which it's done
        Converter converter = factory.getConverter1(1);
        converter.convert(entityImplObj);
        converter.convert(entityImpl2Obj);

        Converter<?> c1 = new ConverterImpl();
        Converter<?> c2 = new ConverterImpl2();
        Converter<?> converter3 = factory.getConverter3(1);
//        Converter<?> could represent any Converter: Converter<Entity>, Converter<EntityImpl>, etc
//        and Converter<Entity> != Converter<EntityImpl> != etc
//        which means that it can't accept any argument
//        converter3.convert(entityImplObj);
//        converter3.convert(entityImpl2Obj);

        Converter<? extends Entity> converter4 = factory.getConverter4(1);
//        Converter<? extends Entity> could represent any Converter: Converter<Entity>, Converter<EntityImpl>, etc
//        and Converter<Entity> != Converter<EntityImpl> != etc
//        which means that it can't accept any argument
//        converter4.convert(entityImplObj);
//        converter4.convert(entityImpl2Obj);

    }

}
