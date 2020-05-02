package com.amairovi.horstmann_java_se_9_for_the_impatient.chapter_6.factory_for_generic;

class Option3 {

    interface Converter {

        void convert(Entity e);

    }

    static class ConverterImpl implements Converter {

        @Override
        public void convert(Entity e) {
            if (e instanceof EntityImpl) {
                EntityImpl ei = (EntityImpl) e;
                convert(ei);
            }
        }

        public void convert(EntityImpl entity) {
        }

    }

    static class ConverterImpl2 implements Converter {

        @Override
        public void convert(Entity e) {
            if (e instanceof EntityImpl2) {
                EntityImpl2 ei = (EntityImpl2) e;
                convert(ei);
            }
        }

        public void convert(EntityImpl2 entity) {
        }

    }

    static class Factory {

        private final Converter converterImpl = new ConverterImpl();
        private final Converter converterImpl2 = new ConverterImpl2();

        Converter getConverter1(int type) {
            return type == 1 ? converterImpl : converterImpl2;
        }

    }

    public static void main(String[] args) {
        Factory factory = new Factory();
        EntityImpl entityImplObj = new EntityImpl();
        EntityImpl2 entityImpl2Obj = new EntityImpl2();

        Converter converter = factory.getConverter1(1);
        converter.convert(entityImplObj);
        converter.convert(entityImpl2Obj);
    }

}
