import { NativeModules } from 'react-native';

type C3POReactNativeBleType = {
    setManufacturerId(manufacturerId: number): void;
    broadcast(uuid: string, payload: number[]): Promise<boolean | Error>;
    stopBroadcast(): Promise<boolean>;
    scan(): Promise<boolean>;
    stopScan(): Promise<boolean>;
};

const { C3POReactNativeBle } = NativeModules;

export default C3POReactNativeBle as C3POReactNativeBleType;
