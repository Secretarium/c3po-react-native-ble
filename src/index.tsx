import { NativeModules } from 'react-native';

type C3POReactNativeBleType = {
    getDeviceName(): Promise<string>;
};

const { C3POReactNativeBle } = NativeModules;

export default C3POReactNativeBle as C3POReactNativeBleType;
